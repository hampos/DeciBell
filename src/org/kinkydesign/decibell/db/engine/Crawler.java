/**
 *   .       .     ..
 *  _| _  _.*|_  _ ||
 * (_](/,(_.|[_)(/,||
 *
 * DeciBell : A Java Tool for creating and managing relational databases.
 *  DeciBell is a Object - Relation database mapper for java applications providing
 * an easy-to-use interface making it easy for the developer to build a relational
 * database and moreover perform database operations easily!
 *  This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Charalampos Chomenides & Pantelis Sopasakis
 *                         kinkyDesign ~ OpenSource Development

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * hampos Att yahoo d0t c0m
 * chvng att mail D0t ntua dd0T gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.kinkydesign.decibell.db.engine;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import org.kinkydesign.decibell.*;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.interfaces.*;
import org.kinkydesign.decibell.db.query.*;
import org.kinkydesign.decibell.db.util.*;

/**
 * <p  align="justify" style="width:60%">
 * This class (Crawler) is a tool that facilitates db-to-java data transactions. Given a
 * row of a table (provided as a <code>ResultSet</code>) and some database connection information
 * (such as the {@link DeciBell connection object}, crawler constructs the object
 * corresponding to that row. Recursion is the key for such a look up. Data contained in
 * relational tables (that correspond to java Collections) and self-references are
 * also retrieved throug the lookup.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Crawler {

    private static final String __NULL__ = RegistrationEngine.__NULL__;
    private final DeciBell db;
    private final StatementPool pool;

    /**
     * <p  align="justify" style="width:60%">
     * Construct a new instance of Crawler. This constructor initializes a new Crawler
     * with the given parameters for the database connection (as a {@link DeciBell} object)
     * and a corresponding pool. The pool is in one-to-one correspondence with the
     * database connection using the relation:<br/>
     * <br/>
     * <code>Statement pool = StatementPool.getPool(db);</code><br/><br/>
     * The pool is requested explicitly to avoid the lookup of the correct pool in
     * the set of pools available. However you may use the simpler variant of this
     * constructor which is
     * </p>
     *
     * @param db
     *      An identifier for the database connection.
     * @param pool
     *      A pool of prepared statements related to the provided database connection.
     */
    public Crawler(final DeciBell db, final StatementPool pool) {
        this.db = db;
        this.pool = pool;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Construct a new instance of Crawler. This constructor initializes a new Crawler
     * with the given database connection object which is identified through a {@link DeciBell }
     * object.
     * </p>
     *
     * @param db
     *      An identifier for the database connection.
     */
    public Crawler(final DeciBell db) {
        this(db, StatementPool.getPool(db));
    }

    /**
     * <p  align="justify" style="width:60%">
     * Crawl in the database to retrieve the component corresponding to some
     * table row following recursively the foreign key directives in the database
     * and thus recursively retrieving all information in the pointed rows (in other
     * tables).
     * </p>
     * @param dbData
     *      A row of some table in the database.
     * @param clazz
     *      The class of the component corresponding to the table holding the initial data (row)
     * @param masterTable
     *      The table in which the master data is found.
     * @return
     *      Retrieved data from the database as a component.
     */
    public Component crawlDatabase(ResultSet dbData, Class<? extends Component> clazz, JTable masterTable) {

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
                String columnName = null;
                for (Proposition proposition : query.getPropositions()) {

                    if (proposition.getTableColumn().isPrimaryKey()) {
                        if (proposition.getTableColumn().isTypeNumeric()) {
                            columnName = (columnName == null) ? groupedFkIterator.next().getColumnName() : columnName;
                            proposition.setInt(dbData.getInt(columnName));
                            ps.setObject(ps_INDEX, dbData.getObject(columnName));
                            ps.setObject(ps_INDEX++, dbData.getObject(columnName));
                            continue;
                        } else {
                            columnName = groupedFkIterator.next().getColumnName();
                            ps.setObject(ps_INDEX, dbData.getString(columnName));
                        }
                    } else {
                        Infinity infinity = new Infinity(db);
                        ps.setObject(ps_INDEX, infinity.getInfinity(proposition));
                    }
                    ps_INDEX++;
                }
                ResultSet newRS = ps.executeQuery();
                newRS.next();

                boolean foundSelfRef = false;
                if (masterTable.isSelfReferencing()) {
                    foundSelfRef = true;
                    List<JTableColumn> selfRefColumns = masterTable.getSelfReferences();
                    for (JTableColumn src : selfRefColumns) {
                        foundSelfRef = foundSelfRef && newRS.getObject(src.getColumnName()).
                                equals(dbData.getObject(src.getReferenceColumn().getColumnName()));
                    }
                }

                if (foundSelfRef) {
                    group.iterator().next().getField().set(component, component);
                } else {
                    group.iterator().next().getField().set(
                            component, crawlDatabase(newRS, group.iterator().next().getReferencesClass(),
                            group.iterator().next().getReferenceTable()));
                }

                newRS.close();
            }
            return component;
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (final InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (final NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (final SQLException ex) {
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


            ResultSet relRs = null;
            try {
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
                    relRs = ps.executeQuery();

                } catch (final SQLException ex) {
                    throw ex;
                } finally {
                    pool.recycleSearch(entry, relationalTable);
                }


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
                    ArrayList tempList;
                    if (component.equals(masterComponent)) {
                        tempList = new ArrayList();
                        onField.set(component, new ArrayList());
                        ((Collection)onField.get(component)).add(component);
                        tempList.add(component);
                    } else {
                        tempList = component.search(db);
                    }

                    collectionJavaType = relRs.getString("METACOLUMN");

                    if (tempList.isEmpty()) {
                        onField.set(masterComponent, Class.forName(collectionJavaType).getConstructor().newInstance());
                    } else if (tempList.size() > 1) {
                        throw new RuntimeException("Single foreign object list has size > 1");
                    }
                    relList.addAll(tempList);

                }

                Collection relCollection = null;

                if (collectionJavaType != null) {
                    Class onClass = Class.forName(collectionJavaType);
                    Constructor con = onClass.getConstructor();
                    Object obj = con.newInstance();
                    relCollection = (Collection) obj;
                    relCollection.addAll(relList);
                } else {
                    Class collectionType = onField.getType();
                    try {
                        relCollection = (Collection) collectionType.getConstructor().newInstance();
                    } catch (NoSuchMethodException nsme) { // The class is abstract (e.g. List) and cannot be instantiated.
                        if (List.class.isAssignableFrom(onField.getType())) {
                            relCollection = Collections.EMPTY_LIST;
                        } else if (Set.class.isAssignableFrom(onField.getType())) {                            
                            relCollection = Collections.EMPTY_SET;
                        } else {
                            relCollection = null;
                        }
                    }
                }

                onField.set(masterComponent, relCollection);

            } catch (final IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (final InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (final NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            } catch (final ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (relRs != null) {
                    try {
                        relRs.close();
                    } catch (final SQLException ex) {
                        throw new RuntimeException("Could not close the result set...", ex);
                    }
                }
            }
        }

    }
}
