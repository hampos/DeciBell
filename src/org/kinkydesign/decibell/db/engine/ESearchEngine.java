/**
 *  Class : ESearchEngine
 *  Date  : May 12, 2010
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.DeciBellReflectUtils;
import org.kinkydesign.decibell.db.util.Infinity;
import org.kinkydesign.decibell.db.util.Pair;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ESearchEngine<T> {

    private final DeciBell db;
    private final StatementPool pool;
    private final ComponentRegistry registry;
    private Map<Field, List> fieldToSearchMap;

    public ESearchEngine(DeciBell db) {
        this.db = db;
        pool = StatementPool.getPool(db.getDbConnector());
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
    }

    public ArrayList<T> search(Component whatToSearch) throws Exception {
        ArrayList<T> resultList = new ArrayList<T>(); // list where the results will be stored
        final Class c = whatToSearch.getClass(); // class corresponding to the searched object
        Table table = (Table) registry.get(c); // table corresponding to searched object
        Pair<PreparedStatement, SQLQuery> entry = pool.getSearch(table);
        SQLQuery query = entry.getValue(); // SELECT query on the table in which whatToSearch is registered...
        PreparedStatement preparedStatement = entry.getKey(); // ..and the related prepared statement.
        final Infinity infinity = new Infinity(db.getDbConnector()); // Easy-to-use infinity values for all propositions


        fieldToSearchMap = new HashMap<Field, List>();
        for (Field foreignKeyField : (ArrayList<Field>) whatToSearch.getForeignKeyFields()) {
            fieldToSearchMap.put(foreignKeyField, search((Component) foreignKeyField.get(whatToSearch)));
        }
        System.out.println(fieldToSearchMap);


        if (!whatToSearch.hasForeignKey()){
             int i = 1; // index used for the prepared statement.
                for (Proposition queryProposition : query.getPropositions()) { // Iterate over all propositions of the query.
                    JTableColumn propositionColumn = queryProposition.getTableColumn();// The column related to the proposition (the proposition is part of the SELECT query)
                    Field propositionColumnField = propositionColumn.getField(); // Field corresponding to the column of the proposition.
                    propositionColumnField.setAccessible(true); // the field might be non-accessible (e.g. private)

                    Object providedValue = propositionColumnField.get(whatToSearch); // The value provided in the field (The field belongs to the given object).

                      // The column is NOT a foreign key column. We discern between two basic cases: Is the column numeri-or-String or XML/object?
                        if (providedValue == null || // The given value (to the non-FK field is NULL
                                (propositionColumn.isTypeNumeric() && ((Double.parseDouble(providedValue.toString())) == Double.parseDouble(propositionColumn.getNumericNull())))) {// The column is numeric and the numeric null is used...
                            // Exploit the infinity to set the table column to "WHATEVER"
                            preparedStatement.setObject(i, infinity.getInfinity(queryProposition), propositionColumn.getColumnType().getType());
                        } else if (!propositionColumn.getColumnType().equals(SQLType.LONG_VARCHAR)) { // Whatever else but not LONG VARCHAR (i.e. XML-serialized object...).
                            // Here we have some value given.... (The value is either numeric or String)
                            preparedStatement.setObject(i, providedValue, propositionColumn.getColumnType().getType()); // set the given value to the prepared statement
                        } else { // This is the case where some value is given and it is some XML-serializable object (non-primitive or String)
                            XStream xstream = new XStream();
                            String xml = xstream.toXML(providedValue);// Serialize the given object to XML
                            preparedStatement.setString(i, xml); // set the serialized XML to the prepared query.
                        }
                    
                    i++;
                }
                ResultSet rs = preparedStatement.executeQuery();
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

                handleForeignKeys(table, rs, whatToSearch, new Component[2], newObj);
                //handleRelationalTables(table, newObj);
                T toBeadded = (T) newObj;
                /*
                 * This check stands only for increased secutiry
                 * TODO: remove the following check in beta version!
                 */
                if (!resultList.contains(toBeadded)) {
                    resultList.add(toBeadded);
                }
            }
        }

        Iterator<Entry<Field, List>> iterator = fieldToSearchMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Field, List> foreignKeyResults = iterator.next();
            Field foreignKey = foreignKeyResults.getKey();
            List FK_results = foreignKeyResults.getValue();

            Iterator resultIterator = FK_results.iterator();
            while (resultIterator.hasNext()) {
                // Fill the query with values:
                int i = 1; // index used for the prepared statement.
                for (Proposition queryProposition : query.getPropositions()) { // Iterate over all propositions of the query.
                    JTableColumn propositionColumn = queryProposition.getTableColumn();// The column related to the proposition (the proposition is part of the SELECT query)
                    Field propositionColumnField = propositionColumn.getField(); // Field corresponding to the column of the proposition.
                    propositionColumnField.setAccessible(true); // the field might be non-accessible (e.g. private)

                    Object providedValue = propositionColumnField.get(whatToSearch); // The value provided in the field (The field belongs to the given object).

                    if (propositionColumn.isForeignKey()) { // Handling a foreign key column...

                        if (propositionColumnField.equals(foreignKey)) {
                            Component foundForeignObject = (Component) resultIterator.next();
                            preparedStatement.setObject(i, ((Field)(foundForeignObject).getPrimaryKeyFields().get(0)).get(foundForeignObject),
                                    propositionColumn.getColumnType().getType());
                        } else {
                            preparedStatement.setObject(i, infinity.getInfinity(queryProposition),
                                    propositionColumn.getColumnType().getType());
                        }
                        
                    } else { // The column is NOT a foreign key column. We discern between two basic cases: Is the column numeri-or-String or XML/object?
                        if (providedValue == null || // The given value (to the non-FK field is NULL
                                (propositionColumn.isTypeNumeric() && ((Double.parseDouble(providedValue.toString())) == Double.parseDouble(propositionColumn.getNumericNull())))) {// The column is numeric and the numeric null is used...
                            // Exploit the infinity to set the table column to "WHATEVER"
                            preparedStatement.setObject(i, infinity.getInfinity(queryProposition), propositionColumn.getColumnType().getType());
                        } else if (!propositionColumn.getColumnType().equals(SQLType.LONG_VARCHAR)) { // Whatever else but not LONG VARCHAR (i.e. XML-serialized object...).
                            // Here we have some value given.... (The value is either numeric or String)
                            preparedStatement.setObject(i, providedValue, propositionColumn.getColumnType().getType()); // set the given value to the prepared statement
                        } else { // This is the case where some value is given and it is some XML-serializable object (non-primitive or String)
                            XStream xstream = new XStream();
                            String xml = xstream.toXML(providedValue);// Serialize the given object to XML
                            preparedStatement.setString(i, xml); // set the serialized XML to the prepared query.
                        }
                    }
                    i++;
                }
                ResultSet rs = preparedStatement.executeQuery();
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

                handleForeignKeys(table, rs, whatToSearch, new Component[2], newObj);
                //handleRelationalTables(table, newObj);
                T toBeadded = (T) newObj;
                /*
                 * This check stands only for increased secutiry
                 * TODO: remove the following check in beta version!
                 */
                if (!resultList.contains(toBeadded)) {
                    resultList.add(toBeadded);
                }
            }

            }
        }

        return resultList;
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
                        System.out.println("Setting " + f.getName());//////
                        f.set(newObj, tempList.get(0));
                    } else {
                        System.out.println("**Setting " + f.getName());
                        for (JTableColumn superCol : registry.get(tempList.get(0).getClass()).getTableColumns()) {
                            Field superField = superCol.getField();
                            superField.setAccessible(true);
                            superField.set(newObj, superField.get(tempList.get(0)));
                        }
                    }
                }
            } else if (component.getClass().equals(whatToSearch.getClass()) && component.equals(whatToSearch)) {
                //handleSelfRefTables(tempComponent, whatToSearch, group, component, newObj);
            }
        }
    }
}
