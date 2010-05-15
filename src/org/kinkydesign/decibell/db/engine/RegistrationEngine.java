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

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.Pair;
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

    // TODO: Fields and methods in this RegistrationEngine need documentation.
    private final DeciBell db;
    private final ComponentRegistry registry;
    private final StatementPool pool;
    private static final String __NULL__ = "__NULL..VALUE__";
    private static final String __DuplicateKey_State__ = "23505";

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
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
        pool = StatementPool.getPool(db.getDbConnector());
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
    public void register(Component toBeWritten)
            throws DuplicateKeyException, ImproperRegistration {
        try {
            save(toBeWritten);
        } catch (SQLException ex) {
            if (ex.getSQLState().equals(__DuplicateKey_State__)) {
                throw new DuplicateKeyException(toBeWritten, db.getDbConnector(), ex);
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save(Component whatToWrite)
            throws DuplicateKeyException, ImproperRegistration, SQLException {

        Class c = whatToWrite.getClass();
        JTable table = registry.get(c);
        Pair<PreparedStatement, SQLQuery> entry = pool.getRegister(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery sqlQuery = entry.getValue();

        int ps_INDEX = 1;
        for (JTableColumn column : sqlQuery.getColumns()) {
            Field columnField = column.getField();
            columnField.setAccessible(true);

             /*
             * CASE: Numeric and not a FK
             */
            if (!column.isForeignKey() && column.isTypeNumeric()) {
                handleSimpleNumerics(whatToWrite, ps, column, columnField, ps_INDEX);
            } /*
             * CASE: String and not a FK
             */ else if (!column.isForeignKey() && column.getColumnType().equals(SQLType.VARCHAR)) {
                handleSimpleStrings(whatToWrite, ps, column, columnField, ps_INDEX);
            } /*
             * CASE: Object (XML) and not FK
             */ else if (!column.isForeignKey() && column.getColumnType().equals(SQLType.LONG_VARCHAR)) {   // XStream (NOT FOREIGN)
                handleSimpleXStream(whatToWrite, ps, column, columnField, ps_INDEX);
            } /*
             * CASE: Foreign Key and not collection
             */ else if (column.isForeignKey() && !Collection.class.isAssignableFrom(columnField.getType())) {
                handleForeignKey(whatToWrite, ps, column, columnField, ps_INDEX);
            } /*
             * CASE: Collection (Will be handled right afterwards)
             */ else if (column.isForeignKey() && Collection.class.isAssignableFrom(columnField.getType())) {
                checkNullity(whatToWrite, columnField);
            } else {
                String message = "Unexpected Condition for column " + column.getColumnName()
                        + " (field :" + columnField.getName() + "). Unhandled condition in RegistrationEngine.";
                throw new RuntimeException(message);
            }
            ps_INDEX++;
        }
        ps.executeUpdate();
        getAutogeneratedFieldValue(whatToWrite, table, ps);
        pool.recycleRegister(entry, table);
        // Finally register data in the relational tables.
        registerCollections(whatToWrite, table);
    }

    private void checkNullity(Component whatToWrite, Field columnField) throws ImproperRegistration {
        try {
            Object valueForField = columnField.get(whatToWrite);
            if (valueForField == null) {
                throw new ImproperRegistration("You are not allowed to register NULL collections in the database using "
                        + "this version of DeciBell.");
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleSimpleNumerics(Component whatTowrite, PreparedStatement ps, JTableColumn column, Field columnField, int ps_INDEX)
            throws ImproperRegistration, SQLException {
        try {
            double valueToBeWritten = Double.parseDouble(columnField.get(whatTowrite).toString());
            double numericNullValue = Double.parseDouble(column.getNumericNull());
            String columnDefaultValue = column.getDefaultValue();

            if (numericNullValue == valueToBeWritten && columnDefaultValue == null) {
                throw new ImproperRegistration("You cannot write the value " + numericNullValue
                        + " in the database (value of field:" + columnField.getName() + ") "
                        + "because it is used as the Numeric Null value for this field in the class "
                        + whatTowrite.getClass().getName() + ".");
            } else if (numericNullValue == valueToBeWritten && columnDefaultValue != null) {
                ps.setObject(ps_INDEX, columnDefaultValue, column.getColumnType().getType());
            } else if (numericNullValue != valueToBeWritten) {
                ps.setObject(ps_INDEX, valueToBeWritten, column.getColumnType().getType());
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleSimpleStrings(Component whatTowrite, PreparedStatement ps, JTableColumn column, Field columnField, int ps_INDEX)
            throws ImproperRegistration, SQLException {
        try {
            Object valueToBeWritten = columnField.get(whatTowrite);
            String columnDefaultValue = column.getDefaultValue();
            if (valueToBeWritten == null && columnDefaultValue == null) {
                throw new ImproperRegistration("You cannot write a null value "
                        + " in the database (value of field:" + columnField.getName() + ") in class"
                        + whatTowrite.getClass().getName() + ". No DEFAULT values found!");
            } else if (valueToBeWritten == null && columnDefaultValue != null) {
                ps.setObject(ps_INDEX, columnDefaultValue, column.getColumnType().getType());
            } else if (valueToBeWritten != null) {
                ps.setObject(ps_INDEX, valueToBeWritten, column.getColumnType().getType());
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleSimpleXStream(Component whatTowrite, PreparedStatement ps, JTableColumn column, Field columnField, int ps_INDEX)
            throws ImproperRegistration, SQLException {
        try {
            Object valueToBeWritten = columnField.get(whatTowrite);
            if (valueToBeWritten == null) {
                ps.setObject(ps_INDEX, __NULL__, column.getColumnType().getType());
            } else {
                XStream xstream = new XStream();
                String serializedObject_XML = xstream.toXML(valueToBeWritten);
                ps.setString(ps_INDEX, serializedObject_XML);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void handleForeignKey(Component whatTowrite, PreparedStatement ps, JTableColumn column, Field columnField, int ps_INDEX)
            throws ImproperRegistration, SQLException {
        try {
            columnField.setAccessible(true);
            Object valueToBeWritten = columnField.get(whatTowrite);
            if (valueToBeWritten == null) {
                throw new ImproperRegistration("The current version of DeciBell does not support "
                        + "registration of NULL values in foreign key fields. The field " + columnField.getName() + " in class "
                        + whatTowrite.getClass().getName() + " is NULL!");
            } else {
                Object foreignKeyValue = getForeignKeyValue(whatTowrite, column, columnField);
                ps.setObject(ps_INDEX, foreignKeyValue, column.getColumnType().getType());
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Object getForeignKeyValue(Component component, JTableColumn column, Field columnField) {
        columnField.setAccessible(true);
        try {
            if (column.isForeignKey()) {
                JTableColumn remoteColumn = column.getReferenceColumn();
                Field remoteField = remoteColumn.getField();
                remoteField.setAccessible(true);
                Object remoteComponent = columnField.get(component);
                return getForeignKeyValue((Component) remoteComponent, remoteColumn, remoteField);
            } else {
                return columnField.get(component);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void registerCollections(Component whatTowrite, JTable table)
            throws ImproperRegistration, SQLException {

        Set<JRelationalTable> relationalTables = table.getRelations();
        Pair<PreparedStatement, SQLQuery> entry = null;
        PreparedStatement ps = null;

        try {
            for (JRelationalTable relationalTable : relationalTables) { // Iterate over all relational tables (i.e. over all collections in MASTER).
                entry = pool.getRegister(relationalTable);
                ps = entry.getKey();
                Field onField = relationalTable.getOnField(); // <-- The collection
                onField.setAccessible(true);
                Collection collection = (Collection) onField.get(whatTowrite);
                for (Object o : collection) {
                    int ps_INDEX = 1;
                    for (JTableColumn column : relationalTable.getTableColumns()) {
                        if (column.getColumnName().equals("METACOLUMN")) {
                            ps.setObject(ps_INDEX, collection.getClass().getName(), SQLType.VARCHAR.getType());
                            ps_INDEX++;
                            continue;
                        }
                        Field f = column.getField();
                        f.setAccessible(true);
                        if (column.getReferenceTable().equals(relationalTable.getMasterTable())) {
                            ps.setObject(ps_INDEX, (Object) f.get(whatTowrite), column.getColumnType().getType());
                        } else {
                            ps.setObject(ps_INDEX, (Object) f.get(o), column.getColumnType().getType());
                        }
                        ps_INDEX++;
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                pool.recycleRegister(entry, relationalTable);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void getAutogeneratedFieldValue(Component component, JTable table, PreparedStatement ps) throws SQLException {
        JTableColumn autoGeneratedColumn = table.getAutogeneratedColumn();
        if (autoGeneratedColumn != null) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            Field autoGeneratedField = autoGeneratedColumn.getField();
            autoGeneratedField.setAccessible(true);
            while (generatedKeys != null && generatedKeys.next()) {
                try {
                    autoGeneratedField.set(component, generatedKeys.getLong(1));
                } catch (Exception ex) {// This should not happen.
                    throw new RuntimeException("Exception caught while attempting to set an autogenerated integer value "
                            + "to the field :" + autoGeneratedField.getName() + ".");
                }
            }
            if (generatedKeys != null && !generatedKeys.isClosed()) {
                generatedKeys.close();
            }
        }
    }
}
