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
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.*;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.*;
import org.kinkydesign.decibell.db.interfaces.*;
import org.kinkydesign.decibell.db.query.*;
import org.kinkydesign.decibell.db.sieve.JSieve;
import org.kinkydesign.decibell.db.util.*;

/**
 * <p  align="justify" style="width:60%">
 * This search engine helps retrieving data from the underlying database as Java
 * objects (which subclass {@link Component }. So what is returned by such a search operation
 * is an ArrayList of Component-type objects.
 * </p>
 * @param <T>
 *      Generic parameter used to identify the searched objects.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Component
 * @see RegistrationEngine
 * @since 0.1.0.0
 * @version 0.1.2.1
 */
public class SearchEngine<T extends Component> {

    private JSieve<T> sieve = null;
    private final DeciBell db;
    private final StatementPool pool;
    private final ComponentRegistry registry;
    private static final String __NULL__ = RegistrationEngine.__NULL__;

    public SearchEngine(final DeciBell db) {
        this.db = db;
        pool = StatementPool.getPool(db);
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
    }

    public SearchEngine(final DeciBell db, final JSieve<T> sieve) {
        this(db);
        this.sieve = sieve;
    }

    /**
     *
     * @param prototype
     *      Object to search for in the database. 
     * @return
     *      List of {@link Component Component-type} objects found in the database
     */
    public ArrayList<T> search(Component prototype) {
        JTable table = registry.get(prototype.getClass());
        try {
            if (isCriteriaMaster(prototype, table)) {
                return doSearchTerminal(prototype);
            } else {                
                return doSearchDeep(prototype);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    private ArrayList<T> doSearchDeep(Component component) throws SQLException{
        throw new UnsupportedOperationException();
    }

    private ArrayList<T> doSearchTerminal(Component component) throws SQLException {

        JTable table = registry.get(component.getClass());
        if (!isCriteriaMaster(component, table)) {
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
                handleTerminalFK(component, ps, column, field, ps_INDEX, whatever);
            } else {
                throw new RuntimeException("[BUG] Unhandled condition in SearchEngine! This should not happen!");
            }
            ps_INDEX++;
        }

        ResultSet resultSet = ps.executeQuery();

        try {
            Crawler crawler = new Crawler(db, pool);
            while (resultSet.next()) {
                T componentFromDB = (T) crawler.crawlDatabase(resultSet, component.getClass(), table);//componentFromDB(resultSet, component.getClass(), table);
                if (sieve == null || sieve.sieve(componentFromDB)) {
                    resultList.add(componentFromDB);
                }
            }
        } catch (final SQLException ex) {
            throw ex;
        } finally {
            pool.recycleSearch(entry, table);
            resultSet.close();
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
            String providedValue = (String) field.get(component);
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
            Object providedObject = field.get(component);
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

    private void handleTerminalFK(Component component, PreparedStatement ps, JTableColumn column, Field field, int ps_INDEX, Object whatever) {        
        try {
            // Terminals do not have declared FK fields. Check it out and proceed with `whatever`
            Object providedValue = field.get(component);
            if (providedValue != null) {
                throw new RuntimeException("[BUG] Not a terminal component!");
            }
            ps.setObject(ps_INDEX, whatever, column.getColumnType().getType());
        } catch (SQLException ex) {
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean isCriteriaMaster(Component component, JTable table) {

        Set<JTableColumn> foreignKeyColumns = table.getForeignKeyColumns();
        Set<JRelationalTable> relationalTables = table.getRelations();

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
                    if (declaredCollection != null && !declaredCollection.isEmpty()) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Could not access a field in :\n" + component, ex);
        }
    }
}
