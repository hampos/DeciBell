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
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.StatementFactory;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class StatementPool {

    public static final int poolSize = 30;
    public static final int queueSize = 50;
    private DbConnector con = null;
    private static Map<DbConnector, StatementPool> pools = new HashMap<DbConnector, StatementPool>();
    private Map<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>> search =
            new HashMap<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>>();
    private Map<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>> searchpk =
            new HashMap<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>>();
    private Map<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>> update =
            new HashMap<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>>();
    private Map<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>> register =
            new HashMap<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>>();
    private Map<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>> delete =
            new HashMap<JTable, ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>>();

    public StatementPool(DbConnector con, int poolSize) {
        this.con = con;
        for (JTable t : ComponentRegistry.getRegistry(con).values()) {
            initTable(t);
        }
        for (JRelationalTable rel : ComponentRegistry.getRegistry(con).getRelationTables()) {
            initRelTable(rel);
        }
        pools.put(con, this);
    }

    public static StatementPool getPool(DbConnector con) {
        return pools.get(con);
    }

    public Entry<PreparedStatement, SQLQuery> getRegister(JTable t) {
        try {
            return register.get(t).take();
        } catch (InterruptedException ex) {
            return getRegister(t);
        }
    }

    public void recycleRegister(Entry<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            register.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Entry<PreparedStatement, SQLQuery> getSearch(JTable t) {
        try {
            return search.get(t).take();
        } catch (InterruptedException ex) {
            return getSearch(t);
        }
    }

    public void recycleSearch(Entry<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            search.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Entry<PreparedStatement, SQLQuery> getSearchPK(JTable t) {
        try {
            return searchpk.get(t).take();
        } catch (InterruptedException ex) {
            return getSearchPK(t);
        }
    }

    public void recycleSearchPK(Entry<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            searchpk.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Entry<PreparedStatement, SQLQuery> getUpdate(JTable t) {
        try {
            return update.get(t).take();
        } catch (InterruptedException ex) {
            return getUpdate(t);
        }
    }

    public void recycleUpdate(Entry<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            update.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Entry<PreparedStatement, SQLQuery> getDelete(JTable t) {
        try {
            return delete.get(t).take();
        } catch (InterruptedException ex) {
            return getDelete(t);
        }
    }

    public void recycleDelete(Entry<PreparedStatement, SQLQuery> pair, JTable t) {
        try {
            pair.getKey().clearParameters();
            delete.get(t).add(pair);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initTable(JTable table) {
        search.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        searchpk.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        register.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        delete.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        update.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        for (int i = 0; i < poolSize; i++) {
            register.get(table).add(StatementFactory.createRegister(table, con));
            delete.get(table).add(StatementFactory.createDelete(table, con));
            search.get(table).add(StatementFactory.createSearch(table, con));
            searchpk.get(table).add(StatementFactory.createSearchPK(table, con));
            update.get(table).add(StatementFactory.createUpdate(table, con));
        }
    }

    private void initRelTable(JRelationalTable table){
        search.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        register.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        delete.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        update.put(table, new ArrayBlockingQueue<Entry<PreparedStatement, SQLQuery>>(queueSize));
        for (int i = 0; i < poolSize; i++) {
            register.get(table).add(StatementFactory.createRegister(table, con));
            delete.get(table).add(StatementFactory.createDelete(table, con));
            search.get(table).add(StatementFactory.createSearch(table, con));
            update.get(table).add(StatementFactory.createUpdate(table, con));
        }
    }
}
