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
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.query.InsertQuery;
import org.kinkydesign.decibell.db.query.SelectQuery;
import org.kinkydesign.decibell.db.util.StatementFactory;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class StatementPool {

    private static Map<DbConnector,StatementPool> pools = new HashMap<DbConnector,StatementPool>();

    private Map<Table, ArrayBlockingQueue<PreparedStatement>> search =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();
    private Map<Table, ArrayBlockingQueue<PreparedStatement>> searchpk =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();
    private Map<Table, ArrayBlockingQueue<PreparedStatement>> update =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();
    private Map<Table, ArrayBlockingQueue<Map.Entry<PreparedStatement,InsertQuery>>> register =
            new HashMap<Table, ArrayBlockingQueue<Map.Entry<PreparedStatement,InsertQuery>>>();
    private Map<Table, ArrayBlockingQueue<PreparedStatement>> delete =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();

    public StatementPool(DbConnector con, int poolSize) {
        for (Table t : ComponentRegistry.getRegistry(con).values()) {
            search.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            searchpk.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            register.put(t, new ArrayBlockingQueue<Map.Entry<PreparedStatement,InsertQuery>>(50));
            delete.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            update.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            for (int i = 0; i < poolSize; i++) {
                register.get(t).add(StatementFactory.createRegister(t, con));
                delete.get(t).add(StatementFactory.createDelete(t, con));
                search.get(t).add(StatementFactory.createSearch(t, con));
                searchpk.get(t).add(StatementFactory.createSearchPK(t, con));
                update.get(t).add(StatementFactory.createUpdate(t, con));
           }
        }
        pools.put(con, this);
    }

    public static StatementPool getPool(DbConnector con){
        return pools.get(con);
    }

    public Map.Entry<PreparedStatement,InsertQuery> getRegister(Table t) {
        try {
            return register.get(t).take();
        } catch (InterruptedException ex) {
            return getRegister(t);
        }
    }

    public void recycleRegister(Map.Entry<PreparedStatement,InsertQuery> ps, Table t) {
        try {
            ps.getKey().clearParameters();
            register.get(t).add(ps);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public PreparedStatement getSearch(Table t) {
        try {
            return search.get(t).take();
        } catch (InterruptedException ex) {
            return getSearch(t);
        }
    }

    public void recycleSearch(PreparedStatement ps, Table t) {
        try {
            ps.clearParameters();
            search.get(t).add(ps);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public PreparedStatement getSearchPK(Table t) {
        try {
            return searchpk.get(t).take();
        } catch (InterruptedException ex) {
            return getSearchPK(t);
        }
    }

    public void recycleSearchPK(PreparedStatement ps, Table t) {
        try {
            ps.clearParameters();
            searchpk.get(t).add(ps);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public PreparedStatement getUpdate(Table t) {
        try {
            return update.get(t).take();
        } catch (InterruptedException ex) {
            return getUpdate(t);
        }
    }

    public void recycleUpdate(PreparedStatement ps, Table t) {
        try {
            ps.clearParameters();
            update.get(t).add(ps);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public PreparedStatement getDelete(Table t) {
        try {
            return delete.get(t).take();
        } catch (InterruptedException ex) {
            return getDelete(t);
        }
    }

    public void recycleDelete(PreparedStatement ps, Table t) {
        try {
            ps.clearParameters();
            delete.get(t).add(ps);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
