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
package org.kinkydesign.decibell.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.Pair;
import org.kinkydesign.decibell.db.util.StatementFactory;

/**
 * a Pool that contains prepared statements for all databases running by DeciBell
 * in the System.
 * Each database is identified by it's DbConnector object.
 * The Pool holds all PreparedStatement objects created by the DbConnectors and
 * associates them with the SQLQuery on which they were created and the JTable
 * in which they belong.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class StatementPool {

    /**
     * The size of each pool. DeciBell totals 5
     * pools of equal capacity.
     */
    public static final int poolSize = 30;
    /**
     * Size of the queue for requests for Prepared Statements.
     */
    public static final int queueSize = 50;

    private DbConnector con = null;

    private static Map<DbConnector, StatementPool> pools = new HashMap<DbConnector, StatementPool>();

    private Map<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>> search =
            new HashMap<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>>();

    private Map<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>> searchpk =
            new HashMap<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>>();

    private Map<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>> update =
            new HashMap<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>>();

    private Map<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>> register =
            new HashMap<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>>();

    private Map<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>> delete =
            new HashMap<JTable, ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>>();

    /**
     * Constructs a new Pool for the given DbConnector and initializes it with the
     * given poolSize.
     * @param con a DbConnector object that represents a database managed by
     * DeciBell.
     */
    public StatementPool(DbConnector con) {
        this.con = con;
        for (JTable t : ComponentRegistry.getRegistry(con).values()) {
            initTable(t);
        }
        for (JRelationalTable rel : ComponentRegistry.getRegistry(con).getRelationTables()) {
            initRelTable(rel);
        }
        pools.put(con, this);
    }

    /**
     * Returns the pool associated with the specified DbConnector.
     * @param con a DbConnector that represents a database managed by DeciBell.
     * @return the StatementPool object associated with the specified DbConnector.
     */
    public static StatementPool getPool(DbConnector con) {
        return pools.get(con);
    }

    /**
     * Returns the pool associated with the specified DbConnector.
     * @param con a {@link DeciBell} object that identifies a database connection.
     * @return the StatementPool object associated with the specified DbConnector.
     */
    public static StatementPool getPool(DeciBell db) {
        return pools.get(db.getDbConnector());
    }

    /**
     * <p  align="justify" style="width:60%">
     * Removes a register operation type PreparedStatement-SQLQuery pair from the pool
     * for a given JTable. This pair holds both the PreparedStatement and the
     * Query specifics to assist in feeding the prepared statement with values.
     * This method will block when the pool is empty of prepared statements for
     * the specific type.
     * </p>
     * @param t the JTable for which a register type prepared statement is needed.
     * @return a PreparedStatement-SQLQuer pair of type Register for the given JTable.
     */
    public Pair<PreparedStatement, SQLQuery> getRegister(JTable t) {
        try {
            return register.get(t).take();
        } catch (InterruptedException ex) {
            return getRegister(t);
        }
    }

    /**
     * Recycles a PreparedStatement-SQLQuery pair of register type in the pool,
     * after it's parameters are cleared. Use of this method is MANDATORY after
     * using getRegister method. If PreparedStatements are not recycled the system
     * will hang when the pool runs empty.
     * @param pair the Entry pair that needs recycling.
     * @param t the JTable in which the Entry belongs.
     */
    public void recycleRegister(Pair<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            register.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Removes a search operation type PreparedStatement-SQLQuery pair from the pool
     * for a given JTable. This pair holds both the PreparedStatement and the
     * Query specifics to assist in feeding the prepared statement with values.
     * This method will block when the pool is empty of prepared statements for
     * the specific type.
     * @param t the JTable for which a search type prepared statement is needed.
     * @return a PreparedStatement-SQLQuer pair of type Search for the given JTable.
     */
    public Pair<PreparedStatement, SQLQuery> getSearch(JTable t) {
        try {
            return (Pair<PreparedStatement, SQLQuery>) search.get(t).take();
        } catch (InterruptedException ex) {
            return getSearch(t);
        }
    }

    /**
     * Recycles a PreparedStatement-SQLQuery pair of search type in the pool,
     * after it's parameters are cleared. Use of this method is MANDATORY after
     * using getSearch method. If PreparedStatements are not recycled the system
     * will hang when the pool runs empty.
     * @param pair the Entry pair that needs recycling.
     * @param t the JTable in which the Entry belongs.
     */
    public void recycleSearch(Pair<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            search.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Removes a search-primary-keys-only operation type PreparedStatement-SQLQuery pair from the pool
     * for a given JTable. This pair holds both the PreparedStatement and the
     * Query specifics to assist in feeding the prepared statement with values.
     * This method will block when the pool is empty of prepared statements for
     * the specific type.
     * @param t the JTable for which a search-primary-keys-only type prepared statement is needed.
     * @return a PreparedStatement-SQLQuer pair of type Search-primary-keys-only for the given JTable.
     */
    public Pair<PreparedStatement, SQLQuery> getSearchPK(JTable t) {
        try {
            return searchpk.get(t).take();
        } catch (InterruptedException ex) {
            return getSearchPK(t);
        }
    }

    /**
     * Recycles a PreparedStatement-SQLQuery pair of search-primary-keys-only type in the pool,
     * after it's parameters are cleared. Use of this method is MANDATORY after
     * using getSearchPK method. If PreparedStatements are not recycled the system
     * will hang when the pool runs empty.
     * @param pair the Entry pair that needs recycling.
     * @param t the JTable in which the Entry belongs.
     */
    public void recycleSearchPK(Pair<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            searchpk.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Removes an update operation type PreparedStatement-SQLQuery pair from the pool
     * for a given JTable. This pair holds both the PreparedStatement and the
     * Query specifics to assist in feeding the prepared statement with values.
     * This method will block when the pool is empty of prepared statements for
     * the specific type.
     * @param t the JTable for which an update type prepared statement is needed.
     * @return a PreparedStatement-SQLQuer pair of type Update for the given JTable.
     */
    public Pair<PreparedStatement, SQLQuery> getUpdate(JTable t) {
        try {
            return update.get(t).take();
        } catch (InterruptedException ex) {
            return getUpdate(t);
        }
    }

    /**
     * Recycles a PreparedStatement-SQLQuery pair of update type in the pool,
     * after it's parameters are cleared. Use of this method is MANDATORY after
     * using getUpdate method. If PreparedStatements are not recycled the system
     * will hang when the pool runs empty.
     * @param pair the Entry pair that needs recycling.
     * @param t the JTable in which the Entry belongs.
     */
    public void recycleUpdate(Pair<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            update.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Removes a delete operation type PreparedStatement-SQLQuery pair from the pool
     * for a given JTable. This pair holds both the PreparedStatement and the
     * Query specifics to assist in feeding the prepared statement with values.
     * This method will block when the pool is empty of prepared statements for
     * the specific type.
     * @param t the JTable for which a delete type prepared statement is needed.
     * @return a PreparedStatement-SQLQuer pair of type Delete for the given JTable.
     */
    public Pair<PreparedStatement, SQLQuery> getDelete(JTable t) {
        try {
            return delete.get(t).take();
        } catch (InterruptedException ex) {
            return getDelete(t);
        }
    }

    /**
     * Recycles a PreparedStatement-SQLQuery pair of delete type in the pool,
     * after it's parameters are cleared. Use of this method is MANDATORY after
     * using getDelete method. If PreparedStatements are not recycled the system
     * will hang when the pool runs empty.
     * @param pair the Entry pair that needs recycling.
     * @param t the JTable in which the Entry belongs.
     */
    public void recycleDelete(Pair<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            delete.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Inializes the pool for a given table by creating PreparedStatement objects
     * for all query types and inserting them to the pool.
     * @param table the JTable to be initiated.
     */
    private void initTable(JTable table) {
        search.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        searchpk.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        register.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        delete.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        update.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        for (int i = 0; i < poolSize; i++) {
            register.get(table).add(StatementFactory.createRegister(table, con));
            delete.get(table).add(StatementFactory.createDelete(table, con));
            search.get(table).add(StatementFactory.createSearch(table, con));
            searchpk.get(table).add(StatementFactory.createSearchPK(table, con));
            update.get(table).add(StatementFactory.createUpdate(table, con));
        }
    }

    /**
     * Inializes the pool for a given relational table by creating PreparedStatement objects
     * for all query types and inserting them to the pool.
     * @param table the JRelationalTable to be initiated.
     */
    private void initRelTable(JRelationalTable table){
        search.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        register.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        delete.put(table, new ArrayBlockingQueue<Pair<PreparedStatement, SQLQuery>>(queueSize));
        for (int i = 0; i < poolSize; i++) {
            register.get(table).add(StatementFactory.createRegister(table, con));
            delete.get(table).add(StatementFactory.createDelete(table, con));
            search.get(table).add(StatementFactory.createSearch(table, con));
        }
    }
}
