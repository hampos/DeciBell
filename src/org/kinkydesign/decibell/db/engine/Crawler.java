package org.kinkydesign.decibell.db.engine;

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.Infinity;
import org.kinkydesign.decibell.db.util.Pair;

public class Crawler {

    private static final String __NULL__ = RegistrationEngine.__NULL__;
    private final DeciBell db;
    private final StatementPool pool;
    private final ComponentRegistry registry;

    public Crawler(DeciBell db, StatementPool pool, ComponentRegistry registry) {
        this.db = db;
        this.pool = pool;
        this.registry = registry;
    }

    public Component crawlDatabase(ResultSet dbData, Class<?> clazz, JTable masterTable) {

        try {
            
            Constructor constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            Component component = (Component) constructor.newInstance();

            Set<JTableColumn> masterTableColumns = masterTable.getTableColumns();

            // This handles normal entries (NON FKs)
            for (JTableColumn column : masterTableColumns) {
                Field columnField = column.getField();
                if (column.isForeignKey()) {
                    continue;
                }

                Object retrievedObject = dbData.getObject(column.getColumnName());
                if (__NULL__.equals(retrievedObject)) {
                    columnField.set(component, null);
                } else {
                    if (column.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        XStream xstream = new XStream();
                        Object valueForField = xstream.fromXML(retrievedObject.toString());
                        columnField.set(component, valueForField);
                    } else {
                        columnField.set(component, retrievedObject);
                    }
                }
            }

            retrieveCollections(dbData, component, masterTable);
            // Now handle FKs:
            Set<Set<JTableColumn>> groupedFKs = masterTable.getForeignColumnsByGroup();

            for (Set<JTableColumn> group : groupedFKs) {
                Iterator<JTableColumn> groupedFkIterator = group.iterator();
                JTable referencesTable = group.iterator().next().getReferenceTable();
                Pair<PreparedStatement, SQLQuery> entry = pool.getSearch(referencesTable);
                PreparedStatement ps = entry.getKey();
                SQLQuery query = entry.getValue();
                pool.recycleSearch(entry, masterTable);

                int ps_INDEX = 1;
                for (Proposition proposition : query.getPropositions()) {
                    if (proposition.getTableColumn().isPrimaryKey()) {
                        if (proposition.getTableColumn().isTypeNumeric()) {
                            ps.setObject(ps_INDEX, dbData.getDouble(groupedFkIterator.next().getColumnName()));
                        } else {
                            ps.setObject(ps_INDEX, dbData.getString(groupedFkIterator.next().getColumnName()));
                        }
                    } else {
                        Infinity infinity = new Infinity(db);
                        ps.setObject(ps_INDEX, infinity.getInfinity(proposition));
                    }
                    ps_INDEX++;
                }                
                ResultSet newRS = ps.executeQuery();
                newRS.next();
                group.iterator().next().getField().set(component, crawlDatabase(newRS, group.iterator().next().getReferencesClass(), group.iterator().next().getReferenceTable()));
                //newRS.close();
            }            
            return component;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }



    private void retrieveCollections(ResultSet dbData, Component masterComponent, JTable masterTable) {
        Set<JRelationalTable> relations = masterTable.getRelations();
        for (JRelationalTable relationalTable : relations) {
            Pair<PreparedStatement, SQLQuery> entry = pool.getSearch(relationalTable);
            PreparedStatement ps = entry.getKey();
            SQLQuery query = entry.getValue();
            Field onField = relationalTable.getOnField();
            int ps_REL_INDEX = 1;

        

            try {
                for (Proposition proposition : query.getPropositions()) {
                    JTableColumn relColumn = proposition.getTableColumn();
                    if (relColumn.getColumnName().equals("METACOLUMN")) {
                        continue;
                    }

                    Field f = relColumn.getField();
                    if (!relationalTable.getMasterTable().equals(relColumn.getReferenceTable())) {
                        Infinity inf = new Infinity(db);
                        ps.setObject(ps_REL_INDEX, inf.getInfinity(proposition), relColumn.getColumnType().getType());
                    } else {
                        ps.setObject(ps_REL_INDEX, dbData.getObject(relColumn.getReferenceColumnName()),
                                relColumn.getColumnType().getType());
                    }
                    ps_REL_INDEX++;
                }
                ResultSet relRs = ps.executeQuery();
                pool.recycleSearch(entry, relationalTable);

                String collectionJavaType = null;

                ArrayList relList = new ArrayList();
                while (relRs.next()) {
                    Class fclass = relationalTable.getSlaveColumns().iterator().next().
                            getField().getDeclaringClass();
                    Constructor fconstuctor = fclass.getDeclaredConstructor();
                    fconstuctor.setAccessible(true);
                    Object fobj = fconstuctor.newInstance();

                    for (JTableColumn col : relationalTable.getSlaveColumns()) {
                        Field ffield = col.getField();
                        ffield.setAccessible(true);
                        ffield.set(fobj, relRs.getObject(col.getColumnName()));
                    }
                    Component component = (Component) fobj;
                    ArrayList tempList = component.search(db);
                    if (tempList.isEmpty()) {
                        onField.set(masterComponent, Class.forName(collectionJavaType).getConstructor().newInstance());
                    } else if (tempList.size() > 1) {
                        throw new RuntimeException("Single foreign object list has size > 1");
                    }
                    relList.addAll(tempList);
                    collectionJavaType = relRs.getString("METACOLUMN");
                }


                Class onClass = Class.forName(collectionJavaType);
                Constructor con = onClass.getConstructor();
                Object obj = con.newInstance();
                Collection relCollection = (Collection) obj;
                relCollection.addAll(relList);
                onField.set(masterComponent, relCollection);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}

