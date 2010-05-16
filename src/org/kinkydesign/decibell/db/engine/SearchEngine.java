package org.kinkydesign.decibell.db.engine;

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.*;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.*;
import org.kinkydesign.decibell.db.interfaces.*;
import org.kinkydesign.decibell.db.query.*;
import org.kinkydesign.decibell.db.util.*;

public class SearchEngine<T> {

    private final DeciBell db;
    private final StatementPool pool;
    private final ComponentRegistry registry;
    private static final String __NULL__ = "__NULL..VALUE__";

    public SearchEngine(final DeciBell db) {
        this.db = db;
        pool = StatementPool.getPool(db.getDbConnector());
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
    }

    public ArrayList<T> search(Component prototype) {
        JTable table = registry.get(prototype.getClass());
        try {
            if (isComponentTerminal(prototype, table)) {
                return doSearchTerminal(prototype);
            } else {
                System.err.println("NOT TERMINAL");
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ArrayList<T> doSearchTerminal(Component component) throws SQLException {

        JTable table = registry.get(component.getClass());
        if (!isComponentTerminal(component, table)) {
            return null;
        }

        ArrayList<T> resultList = new ArrayList<T>();

        Pair<PreparedStatement, SQLQuery> entry = pool.getSearch(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery query = entry.getValue();
        Infinity infinity = new Infinity(db);

        int ps_INDEX = 1;

        for (Proposition proposition : query.getPropositions()) {
            Object whatever = infinity.getInfinity(proposition);
            JTableColumn column = proposition.getTableColumn();
            Field field = column.getField();

            if (!column.isForeignKey() && column.isTypeNumeric()) {
                handleTerminalNumeric(component, ps, column, field, ps_INDEX, whatever);
            } else if (!column.isForeignKey() && column.getColumnType().equals(SQLType.VARCHAR)) {
                handleTerminalString(component, ps, column, field, ps_INDEX, whatever);
            } else if (!column.isForeignKey() && column.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                handleTerminalXML(component, ps, column, field, ps_INDEX, whatever);
            } else if (column.isForeignKey()) {
                throw new RuntimeException("This method applied only to terminal components. This "
                        + "shoudl not have happened and is a BUG!");
            } else {
                throw new RuntimeException("WTF!?");
            }
            ps_INDEX++;
        }

        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            T componentFromDB = componentFromDB(resultSet, component.getClass(), table);
            resultList.add(componentFromDB);
        }
        return resultList;
    }

    private void handleTerminalNumeric(Component component, PreparedStatement ps, JTableColumn column, Field field, int ps_INDEX, Object whatever)
            throws SQLException {
        try {
            double providedValue = Double.parseDouble(field.get(component).toString());
            double numericNullValue = Double.parseDouble(column.getNumericNull());
            if (providedValue == numericNullValue) {
                ps.setObject(ps_INDEX, whatever, column.getColumnType().getType());
            } else {
                ps.setObject(ps_INDEX, providedValue, column.getColumnType().getType());
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleTerminalString(Component component, PreparedStatement ps, JTableColumn column, Field field, int ps_INDEX, Object whatever)
            throws SQLException {
        try {
            String providedValue = (String )field.get(component);
            if (providedValue == null) {
                ps.setObject(ps_INDEX, whatever, column.getColumnType().getType());
            } else {
                ps.setObject(ps_INDEX, providedValue, column.getColumnType().getType());
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleTerminalXML(Component component, PreparedStatement ps, JTableColumn column, Field field, int ps_INDEX, Object whatever)
            throws SQLException {
        try {
            Object providedObject = field.get(field.get(component));
            if (providedObject == null) {
                ps.setObject(ps_INDEX, whatever, column.getColumnType().getType());
            } else {
                XStream xstream = new XStream();
                String xmlSerializedObject = xstream.toXML(providedObject);
                ps.setObject(ps_INDEX, xmlSerializedObject, column.getColumnType().getType());
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

    }

    private boolean isComponentTerminal(Component component, JTable table) {

        Set<JTableColumn> foreignKeyColumns = table.getForeignKeyColumns();
        Set<JRelationalTable> relationalTables = table.getRelations();
        boolean isTerminal = true;

        try {
            if (foreignKeyColumns.isEmpty() && relationalTables.isEmpty()) {
                return true; // Surely terminal if no FKs at all!
            } else {
                /*
                 * 1. Check all foreign key columns, i.e.
                 *    all FK fields of the object.
                 */
                for (JTableColumn columnFK : foreignKeyColumns) {
                    Field fieldFK = columnFK.getField();
                    if (fieldFK.get(component) != null) {
                        return false;
                    }
                }
                /*
                 * 2. Check all MTM-relations
                 */
                for (JRelationalTable relationalTable : relationalTables) {
                    Field onField = relationalTable.getOnField();
                    Collection declaredCollection = (Collection) onField.get(component);
                    if (declaredCollection != null || !declaredCollection.isEmpty()) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Could not access a field in :\n" + component, ex);
        }
    }

    private T componentFromDB(ResultSet dbData, Class<?> clazz, JTable masterTable) throws SQLException {
        try {
            Constructor constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            Component component = (Component) constructor.newInstance();

            Set<JTableColumn> tableColumns = masterTable.getTableColumns();

            for (JTableColumn column : tableColumns) {
                Field field = column.getField();
                if (column.isForeignKey()) {
                    continue;
                }

                Object retrievedObject = dbData.getObject(column.getColumnName());
                if (column.getColumnType().equals(SQLType.LONG_VARCHAR)) {                    
                    if (retrievedObject.equals(__NULL__)) {
                        field.set(component, null);
                    } else {
                        XStream xstream = new XStream();
                        Object valueForField = xstream.fromXML(retrievedObject.toString());
                        field.set(component, valueForField);
                    }
                } else {
                    field.set(component, retrievedObject);
                }
            }
            return (T) component;

        } catch (InstantiationException ex) {
            throw new RuntimeException("Either the class " + clazz.getName() + "is abstract or "
                    + "the requested constructor (with no parameters) does not exist.", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("this Constructor object enforces Java language "
                    + "access control and the underlying constructor is inaccessible.", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("The constructor " + clazz.getName() + "() threw "
                    + "an exception upon invokation.", ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("A matching method was not found", ex);
        }
        
    }
}

