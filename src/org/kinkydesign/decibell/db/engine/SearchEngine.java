/**
 *  Class : SearchEngine
 *  Date  : May 1, 2010
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

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.DeciBellReflectUtils;
import org.kinkydesign.decibell.db.util.Infinity;
import org.kinkydesign.decibell.db.util.Pair;

/**
 * <p  align="justify" style="width:60%">
 * This search engine helps retrieving data from the underlying database as Java
 * objects (which subclass {@link Component }. So what is returned by such a search operation
 * is an ArrayList of Component-type objects.
 * </p
 * @param <T>
 *      Generic parameter used to identify the searched objects.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Component
 * @see RegistrationEngine
 * @since 0.1.0.0
 */
public class SearchEngine<T> {

    /**
     *
     * <p  align="justify" style="width:60%">
     * Search penetration parameter for search operations that include self-referencign tables.
     * </p>
     * <p  align="justify" style="width:60%">
     * It is quite normal in SQL databases that an entity points to itself. This procedure
     * has a start and an end and it is implied that there is some entry which points to itself.
     * It is not hard to understand that: Suppose we created a table <code>TABLE_A</code> with a column
     * <code>colSelfPointing</code> that points to the primary key of the table, namely,
     * <code>PK</code>. The table is created without any data, so the first entry has to point
     * to some existing entry of the table and since there is no other, it has to point to itself.
     * Have we added the first entry, this contraint is now weakened; for example, the second
     * entry, is possible to reference the first one.
     * </p>
     * <p  align="justify" style="width:60%">
     * However the facts in Java<font size="-2"><sup>TM</sup></font> are a little
     * different since, if an object holds a field of the same type with itself which
     * is not <code>null</code> then that field as an object may hold some other not
     * <code>null</code> field. There is no programmatic way to have an object which
     * has a field equal to that before the first object is initialized because
     * this compiles into a {@link StackOverflowError Stack Overflow}!
     * It is not possible to store infinite many objects in the memory(heap) with a
     * class-field relationship. Although, we can have a finite series of such relations. The
     * length of this series is specified by this integer number.
     * </p>
     * <p  align="justify" style="width:60%">
     * Once all objects have been initialized, we use the method
     * {@link SearchEngine#fixSelfReferences(org.kinkydesign.decibell.Component)
     * fixSelfReferences} to convert this finite series into an infinite one, by
     * setting <code>object.field = object</code> once it identifies that
     * <code>object.field.equals(object)==true</code>.
     * </p>
     *
     *
     */
    protected static final int PENETRATION = 2; // Attention! This number should not exceed (StatementPool.poolSize-1)
    private final DeciBell db;
    private final StatementPool pool;
    private final ComponentRegistry registry;

    public SearchEngine(final DeciBell db) {
        this.db = db;
        pool = StatementPool.getPool(db.getDbConnector());
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
    }

    /**
     *
     * @param whatToSearch
     *      Object to search for in the database.
     * @return
     *      List of objects found in the database
     */
    public ArrayList<T> search(Component whatToSearch) {
        return search(whatToSearch, new Component[PENETRATION]);
    }

    // TODO: Some refactoring is still needed here...
    // TODO: Support search for hierarchical data.
    private ArrayList<T> search(Component whatToSearch, Component[] tempComponent) {
        ArrayList<T> resultList = new ArrayList<T>();
        Class c = whatToSearch.getClass();

        Table table = (Table) registry.get(c);
        Pair<PreparedStatement, SQLQuery> entry = pool.getSearch(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery query = entry.getValue();

        //TODO: Add comments inside the method content to tell what every line does...
        try {
            ResultSet rs = acquireResults(query, ps, whatToSearch);
            pool.recycleSearch(entry, table);
            Constructor constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            while (rs.next() != false) {
                Object newObj = constructor.newInstance();
                for (JTableColumn col : table.getTableColumns()) {
                    Field f = col.getField();
                    f.setAccessible(true);
                    if (col.isForeignKey()) {
                        continue;
                    } else if (col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        XStream xstream = new XStream();
                        Object xobj = null;
                        try {
                            xobj = xstream.fromXML((String) rs.getObject(col.getColumnName()));
                        } catch (NullPointerException ex) {
                            xobj = null;
                        }
                        f.set(newObj, xobj);
                    } else {
                        f.set(newObj, rs.getObject(col.getColumnName()));
                    }
                }

                handleForeignKeys(table, rs, whatToSearch, tempComponent, newObj);
                handleRelationalTables(table, newObj);
                T toBeadded = (T) fixSelfReferences((Component) newObj);
                /*
                 * This check stands only for increased secutiry
                 * TODO: remove the following check in beta version!
                 */
                if (!resultList.contains(toBeadded)) {
                    resultList.add(toBeadded);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return resultList;
    }

    private ResultSet acquireResults(SQLQuery query, PreparedStatement ps, Component whatToSearch) throws Exception {
        int i = 1;
        for (Proposition p : query.getPropositions()) {
            JTableColumn col = p.getTableColumn();
            Field field = col.getField();
            field.setAccessible(true);
            Object obj = null;

            try {
                obj = field.get(whatToSearch);

                if (col.isForeignKey()) {
                    Field f = col.getReferenceColumn().getField();
                    f.setAccessible(true);

                    if (!col.isTypeNumeric()) { // The column is a non-numeric foreign key...
                        Field remotePKfield = (Field) ((Component) obj).getPrimaryKeyFields().get(0);
                        if (remotePKfield.get(obj)==null){
                            Infinity inf = new Infinity(db.getDbConnector());
                            ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                        }else{
                            ps.setObject(i, (Object) remotePKfield.get(obj), col.getColumnType().getType());
                        }                        
                    } else {
                        if (col.isTypeNumeric() && ((Double.parseDouble(f.get(obj).toString())) == Double.parseDouble(col.getNumericNull()))) {
                            Infinity inf = new Infinity(db.getDbConnector());
                            ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                        } else if (col.isTypeNumeric() && !((Double.parseDouble(f.get(obj).toString())) == Double.parseDouble(col.getNumericNull()))) {
                            ps.setObject(i, f.get(obj), col.getColumnType().getType());
                        }
                    }

                } else if (obj == null
                        || (col.isTypeNumeric() && ((Double.parseDouble(obj.toString())) == Double.parseDouble(col.getNumericNull())))) {
                    Infinity inf = new Infinity(db.getDbConnector());
                    ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                    ps.setObject(i, obj, col.getColumnType().getType());
                } else {
                    XStream xstream = new XStream();
                    String xml = xstream.toXML(obj);
                    ps.setString(i, xml);
                }
            } catch (NullPointerException ex) {
                Infinity inf = new Infinity(db.getDbConnector());
                ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
            }
            i++;
        }
        return ps.executeQuery();
    }

    void handleForeignKeys(
            Table table,
            ResultSet rs,
            Component whatToSearch,
            Component[] tempComponent,
            Object newObj) throws Exception {

        for (Set<JTableColumn> group : table.getForeignColumnsByGroup()) {
            /*
             * Retrieve the object of the SAME type which is referenced by a
             * self-referencing foreign column.
             */
            Class refClass = group.iterator().next().getReferencesClass();
            Constructor refConstructor = refClass.getDeclaredConstructor();
            refConstructor.setAccessible(true);
            Object refObj = refConstructor.newInstance();

            for (JTableColumn col : group) {
                Field f = col.getReferenceColumn().getField();
                f.setAccessible(true);
                f.set(refObj, rs.getObject(col.getColumnName()));
            }
            Component component = (Component) refObj;

            /*
             * Foreign Key but NOT a self-reference!
             */
            if ((component.getClass().equals(whatToSearch.getClass()) && !component.equals(whatToSearch))
                    || !component.getClass().equals(whatToSearch.getClass())) {
                ArrayList tempList = component.search(db);

                if (tempList.size() > 1) {
                    throw new RuntimeException("Single foreign object list has size > 1");
                } else if (tempList.size() == 1) {
                    /*
                     * We discern between 2 cases. Firstly the foreign key points to
                     * a foreign table which does not correspond to a superclass of
                     * newObj(whatToSearch), and secondly a foreign table that really
                     * is a superclass of the searched object. In the second case
                     * all superfields of newObj have to be set.
                     */
                    Field f = group.iterator().next().getField();
                    f.setAccessible(true);
                    //@Old:  ArrayList newObjFields = new ArrayList(Arrays.asList(newObj.getClass().getDeclaredFields()));
                    Set<Field> newObjFields = DeciBellReflectUtils.getAllFields(newObj.getClass(), true);
                    if (newObjFields.contains(f)) {
                        //System.out.println("Setting " + f.getName());//////
                        f.set(newObj, tempList.get(0));
                    } else {
                        //System.out.println("**Setting " + f.getName());
                        for (JTableColumn superCol : registry.get(tempList.get(0).getClass()).getTableColumns()) {
                            Field superField = superCol.getField();
                            superField.setAccessible(true);
                            superField.set(newObj, superField.get(tempList.get(0)));
                        }
                    }
                }
            } else if (component.getClass().equals(whatToSearch.getClass()) && component.equals(whatToSearch)) {
                handleSelfRefTables(tempComponent, whatToSearch, group, component, newObj);
            }
        }
    }

    /**
     *
     * @param tempComponent
     * @param whatToSearch
     * @param group
     * @param component
     * @param newObj
     *      This is an input and output parameter.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void handleSelfRefTables(
            Component[] tempComponent,
            Component whatToSearch,
            Set<JTableColumn> group,
            Component component,
            Object newObj) throws IllegalArgumentException, IllegalAccessException {
        // <editor-fold defaultstate="collapsed" desc="Self Referencing FK">
                        /*
         * A boolean flag which specifies whether the search is complete or
         * if we need to search into other objects as well.
         */
        boolean bin = false;

        /*
         * Examine whether the search should continue the tree spanning
         * or quit here. This is parametrized by means of the integer PENETRATION.
         */
        if (hasNullElement(tempComponent)) {
            bin = true;

        } else {
            for (Component compo : tempComponent) {
                bin = bin || (!component.equals(compo));
            }
        }

        if (bin) {
            for (int k = 0; k
                    < PENETRATION - 1; k++) {
                tempComponent[k] = tempComponent[k + 1];
            }
            tempComponent[PENETRATION - 1] = component;
            ArrayList tempList = search(whatToSearch, tempComponent);

            if (tempList.size() > 1) {
                throw new RuntimeException("Single foreign object list has size > 1");
            } else if (tempList.size() == 1) {
                Field f = group.iterator().next().getField();
                f.setAccessible(true);
                f.set(newObj, tempList.get(0));
            }
        }
        /*
         * Well...we need to study this case a little!
         */
        // </editor-fold>
    }

    private void handleRelationalTables(Table table, Object newObj) throws Exception {
        /**
         * Perform search into the relational tables.
         */
        // <editor-fold defaultstate="collapsed" desc="Relational Tables">
        for (JRelationalTable relTable : table.getRelations()) {
            ArrayList relList = new ArrayList();
            Entry<PreparedStatement, SQLQuery> fentry = pool.getSearch(relTable);
            PreparedStatement ps = fentry.getKey();
            SQLQuery query = fentry.getValue();
            Field field = relTable.getOnField();
            field.setAccessible(true);
            int i = 1;
            for (Proposition p : query.getPropositions()) {
                JTableColumn col = p.getTableColumn();
                if (col.getColumnName().equals("METACOLUMN")) {
                    continue;
                }
                Field f = col.getField();
                f.setAccessible(true);
                try {
                    if (!col.getReferenceTable().equals(relTable.getMasterTable())) {
                        Infinity inf = new Infinity(db.getDbConnector());
                        ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());

                    } else {
                        Object obj = f.get(newObj);
                        //TODO: NumericNull must be checked here.
                        if (obj == null) {
                            Infinity inf = new Infinity(db.getDbConnector());
                            ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                        }
                        ps.setObject(i, obj, col.getColumnType().getType());
                    }
                } catch (NullPointerException ex) {
                    Infinity inf = new Infinity(db.getDbConnector());
                    ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                }
                i++;
            }
            ResultSet relRs = ps.executeQuery();
            pool.recycleSearch(fentry, relTable);
            String collectionJavaType = null;
            while (relRs.next() != false) {
                Class fclass = relTable.getSlaveColumns().iterator().next().
                        getField().getDeclaringClass();
                //TODO: Maybe declared Constructor is the way to go?
                Constructor fconstuctor = fclass.getConstructor();
                fconstuctor.setAccessible(true);
                Object fobj = fconstuctor.newInstance();

                for (JTableColumn col : relTable.getSlaveColumns()) {
                    Field ffield = col.getField();
                    ffield.setAccessible(true);
                    ffield.set(fobj, relRs.getObject(col.getColumnName()));
                }
                Component component = (Component) fobj;
                ArrayList tempList = component.search(db);
                if (tempList.isEmpty()) {
                    throw new RuntimeException("Empty list on search for foreign objects");
                } else if (tempList.size() > 1) {
                    throw new RuntimeException("Single foreign object list has size > 1");
                }
                relList.addAll(tempList);
                collectionJavaType = relRs.getString("METACOLUMN");
            }

            Field onField = relTable.getOnField();
            Class onClass = Class.forName(collectionJavaType);
            Constructor con = onClass.getConstructor();
            Object obj = con.newInstance();
            Collection relCollection = (Collection) obj;
            relCollection.addAll(relList);
            onField.set(newObj, relCollection);
        }// </editor-fold>
    }

    private boolean hasNullElement(Object[] array) {
        for (Object o : array) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Post-processing for the method {@link Component#search(org.kinkydesign.decibell.DeciBell)
     * search()}. Fixes self-references, i.e. foreign keys pointing from an object to
     * it self. The false-hierarchical structure that appears it the object which can be visualized
     * by the following schema:
     * </p>
     * <blockquote><pre>
     * obj_a
     *   L obj_a.x = obj_b (and obj_b is equal to obj_a)
     *     L ...
     *       L ...
     * </pre></blockquote>
     * <p  align="justify" style="width:60%">
     * is converted into the an object that explicitly contains itself as a field:
     * </p>
     * <blockquote><pre>
     * obj_a
     *   L obj_a.x = obj_a (points directly to itself)
     * </pre></blockquote>
     *
     * @param input
     *      <p  align="justify" style="width:60%">
     *      Component with self-references to be processed.
     *      </p>
     *
     * @return
     *      <p  align="justify" style="width:60%">
     *      Post-processed object that contains itself as a field
     *      </p>
     */
    private Component fixSelfReferences(Component input) {
        Component output = input;
        if (input == null) {
            throw new NullPointerException("Cannot fix self-references in a null component!");
        }
        for (Field foreignField : (List<Field>) input.getForeignKeyFields()) {
            foreignField.setAccessible(true);
            try {
                // ...foreign key is a self-reference pointing to the same entry (this)
                if (foreignField.getType().equals(input.getClass())
                        && input.equals(foreignField.get(input))//foreignField.get(input).equals(input)
                        ) {
                    // Make the field to point to itself!
                    foreignField.set(output, output);
                }

            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Unexpected condition - Field '" + foreignField.getName() + "' was supposed "
                        + "to be accessible. Method could not access the field!", ex);
            }
        }
        return output;
    }
}
