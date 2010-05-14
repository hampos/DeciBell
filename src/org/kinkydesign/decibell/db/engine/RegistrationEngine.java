package org.kinkydesign.decibell.db.engine;

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

public class RegistrationEngine {

    private final DeciBell db;
    private final ComponentRegistry registry;
    private final StatementPool pool;
    private static final String __NULL__ = "__NULL..VALUE__";

    public RegistrationEngine(final DeciBell db) {
        this.db = db;
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
        pool = StatementPool.getPool(db.getDbConnector());
    }

    public void register(Component toBeWritten)
            throws DuplicateKeyException, ImproperRegistration, SQLException {
        registerPart(toBeWritten);
    }

    private void registerPart( /* what to write:     */Component whatToWrite)
            throws DuplicateKeyException, ImproperRegistration, SQLException {

        Class c = whatToWrite.getClass();
        Table table = (Table) registry.get(c);
        Entry<PreparedStatement, SQLQuery> entry = pool.getRegister(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery sqlQuery = entry.getValue();

        int ps_INDEX = 1;
        for (JTableColumn column : sqlQuery.getColumns()) {
            Field columnField = column.getField();
            columnField.setAccessible(true);

            if (!column.isForeignKey()           // NOT FOREIGN KEY
                    && column.isTypeNumeric()) { // Numeric Type Entries (NOT FOREIGN)
                handleSimpleNumerics(whatToWrite, ps, column, columnField, ps_INDEX);
            } else if (!column.isForeignKey()                            // NOT FOREIGN KEY
                    && column.getColumnType().equals(SQLType.VARCHAR)) { // String Type Entries (NOT FOREIGN)
                handleSimpleStrings(whatToWrite, ps, column, columnField, ps_INDEX);
            } else if (!column.isForeignKey()                                   // NOT FOREIGN KEY
                    && column.getColumnType().equals(SQLType.LONG_VARCHAR)) {   // XStream (NOT FOREIGN)
                handleSimpleXStream(whatToWrite, ps, column, columnField, ps_INDEX);
            } else if (column.isForeignKey()                                        // FOREIGN KEY
                    && !Collection.class.isAssignableFrom(columnField.getType())) { // but NOT COLLECTION
                handleForeignKey(whatToWrite, ps, column, columnField, ps_INDEX);
            }
            ps_INDEX++;
        }
        ps.executeUpdate();
        pool.recycleRegister(entry, table);
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
            if (valueToBeWritten == null){
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
    }
}
