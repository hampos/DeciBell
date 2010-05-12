/**
 *  Class : RegistrationEngine
 *  Date  : May 2, 2010
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.DeciBellReflectUtils;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 * <p  align="justify" style="width:60%">
 * The Registration engine undertakes the task of registrering data into a database
 * identified by a {@link DeciBell } object.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @since 0.1.0.0
 */
public class RegistrationEngine {

    private final DeciBell db;

    /**
     * <p  align="justify" style="width:60%">
     * Initialize a new Registration engine providing the {@link DeciBell } object
     * which identifies a datbase connection.
     * </p>
     * @param db
     *      The DeciBell object that identifies the connections the engine will use
     *      to register the data.
     */
    public RegistrationEngine(final DeciBell db) {
        this.db = db;
    }

    /**
     * Register a component in a database identified by a {@link DeciBell } object.
     * @param toBeWritten
     *      The object to be registered in the database. Note that only objects which
     *      can be cast as Components is possible to be written in the database.
     * @throws DuplicateKeyException
     *      In case the object is already in the database. Formally, this exception is
     *      thrown is the following conditions do hold: <code>toBeWritten.search(db).size==1</code>
     *      and <code>toBeWritten.search(db).get(0).equals(toBeWritten)</code>.
     * @throws ImproperRegistration
     *      A component cannot be written in the database if it has a Collection-type
     *      field which is <code>null</code>. This is because, when a collection
     *      is written in the database, DeciBell also stores the specific implementation
     *      of that list, to be able afterwards to retrieve these data in the proper form.
     */
    public void register(Component toBeWritten) throws DuplicateKeyException, ImproperRegistration {
        registerPart(toBeWritten);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Given an object (that can be cast as component), which is
     * an instance of some <b>indirect</b> subclass of Component (e.g. Component &lt; Entity &lt;
     * SubEntity), returns the super-object (with the proper type-casting).
     * </p>
     * @param obj
     *      Given component.
     * @return
     *      The super-object corresponding to the given component.
     */
    private Object getSuperComponent(Object obj) {
        try {
            Component.class.cast(obj);
        } catch (Exception ex) {
            // This is not a component class
            throw new IllegalArgumentException("This is not a component!");
        }

        if (obj.getClass().getSuperclass() == Component.class) {
            // This is a direct subclass of Compoenent
            return obj;
        }

        //@Old: Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        Set<Field> superFields = DeciBellReflectUtils.getAllFields(obj.getClass().getSuperclass(), true);

        try {
            // Note: DeciBell cannot instantiate abstract classes...
            Object superObject =
                    obj.getClass().getSuperclass().getDeclaredConstructor().newInstance();
            for (Field superField : superFields) {
                superField.setAccessible(true);
                superField.set(superObject, superField.get(obj));
            }
            return superObject;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace(); // for debugging purposes...
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    private void registerPart( /* what to write:     */Component whatToWrite)
            throws DuplicateKeyException, ImproperRegistration {

        /*
         * Check if the component is a direct subclass of Component,
         * otherwise handle its super- counterparts first.
         * The following lines do fix the bug related to registration of an indirect
         * subclass of component, yet plenty of labor is needed to have search
         * working for such cases...
         */
        if (whatToWrite.getClass().getSuperclass() != Component.class) {
            registerPart((Component) getSuperComponent(whatToWrite));
        }

        // TODO: Check if the problem with registration of indirect subclasses of Component is really fixed.
        /*
         * 1. Is the entity an indirect subclass of Component?
         *     L if yes,
         *          Get the corresponding super-object.
         * 2. Write the direct sublass of Component into the DB and goto 1.
         */
        Class c = whatToWrite.getClass();
        ComponentRegistry registry = ComponentRegistry.getRegistry(db.getDbConnector());
        Table table = (Table) registry.get(c);
        StatementPool pool = StatementPool.getPool(db.getDbConnector());
        Entry<PreparedStatement, SQLQuery> entry = pool.getRegister(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery sqlQuery = entry.getValue();

        try {
            int i = 1;
            /*
             * Here we feed the prepared statement:
             */
            for (JTableColumn col : sqlQuery.getColumns()) {
                Field field = col.getField();
                field.setAccessible(true);
                if (col.isForeignKey() && !col.isPrimaryKey()) { // <== foreign but not primary key!
                    Field f = col.getReferenceColumn().getField();
                    f.setAccessible(true);
                    if (field.get(whatToWrite) == null) {
                        throw new ImproperRegistration("You are not allowed to register NULL values in a foreign key field (in this version). This feature "
                                + "will be supported in the beta version of DeciBell");
                        // ps.setNull(i, TypeMap.getSQLType(field.getType()).getType());
                    } else if (f.get(field.get(whatToWrite)) == null) {
                        ps.setObject(i, (Object) col.getDefaultValue(), col.getColumnType().getType());
                    } else {
                        ps.setObject(i, (Object) f.get(field.get(whatToWrite)), col.getColumnType().getType());
                    }
                } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                    if (col.isForeignKey() && col.isPrimaryKey()) { // <== Both foreign and primary key!
                        try {
                            ps.setObject(i, field.get(whatToWrite), col.getColumnType().getType());
                        } catch (Exception ex) {// SQLException or ClassCastException
                            ps.setObject(i, ((Field) ((Component) field.get(whatToWrite)).getPrimaryKeyFields().get(0)).
                                    get(field.get(whatToWrite)), col.getColumnType().getType());
                        }

                    } else if (field.get(whatToWrite) == null) {
                        if (col.getDefaultValue()!=null){
                            ps.setObject(i, (Object) col.getDefaultValue(), col.getColumnType().getType());
                        }else{
                            throw new ImproperRegistration("You cannot register a null value in the databset using " +
                                    "this version of DeciBell. This feature will be supported in out Beta version.", new NullPointerException());
                        }
                    } else { // <~ Usual registration of some object...
                        Object valueToBeWritten = field.get(whatToWrite);
                        if (col.isTypeNumeric() && Double.parseDouble(valueToBeWritten.toString())
                                ==Double.parseDouble(col.getNumericNull())){
                            ps.setObject(i, col.getDefaultValue(), col.getColumnType().getType());
                        }else{
                            ps.setObject(i, valueToBeWritten, col.getColumnType().getType());
                        }
                    }
                } else {
                    XStream xstream = new XStream();
                    String xml = xstream.toXML(field.get(whatToWrite));
                    ps.setString(i, xml);
                }
                i++;
            }
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();

            Field autoGenField = whatToWrite.getAutogeneratedField();
            if (autoGenField != null) {
                autoGenField.setAccessible(true);
                while (generatedKeys != null && generatedKeys.next()) {
                    autoGenField.set(whatToWrite, generatedKeys.getInt(1));
                    break;
                }
            }
            pool.recycleRegister(entry, table);

            for (JRelationalTable relTable : table.getRelations()) {
                entry = pool.getRegister(relTable);
                ps = entry.getKey();
                Field field = relTable.getOnField();
                field.setAccessible(true);
                Object obj = null;

                try {
                    obj = field.get(whatToWrite);
                } catch (NullPointerException ex) { // <== what does it mean?
                    continue;
                }
                if (obj == null) {
                    whatToWrite.delete(db); // <= Important: deletes what is already registered!
                    throw new ImproperRegistration("For field " + field.getName() + " in " + field.getDeclaringClass().getName() + ". "
                            + "You cannot add an object with a null collection in it!");
                }
                Collection collection = (Collection) obj;

                for (Object o : collection) {
                    i = 1;
                    for (JTableColumn col : relTable.getTableColumns()) {
                        if (col.getColumnName().equals("METACOLUMN")) {
                            Field f = relTable.getOnField();
                            f.setAccessible(true);
                            ps.setObject(i, (Object) f.get(whatToWrite).getClass().getName(), SQLType.VARCHAR.getType());
                            i++;
                            continue;
                        }
                        Field f = col.getField();
                        f.setAccessible(true);
                        if (col.getReferenceTable().equals(relTable.getMasterTable())) {
                            ps.setObject(i, (Object) f.get(whatToWrite), col.getColumnType().getType());
                        } else {
                            ps.setObject(i, (Object) f.get(o), col.getColumnType().getType());
                        }
                        i++;
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                pool.recycleRegister(entry, relTable);
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                throw new DuplicateKeyException(whatToWrite, db.getDbConnector(), ex);
            } else {
                throw new RuntimeException(ex);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }
}
