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
import org.kinkydesign.decibell.db.table.Table;
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
    private Map<Table, ArrayBlockingQueue<PreparedStatement>> update =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();
    private Map<Table, ArrayBlockingQueue<PreparedStatement>> register =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();
    private Map<Table, ArrayBlockingQueue<PreparedStatement>> delete =
            new HashMap<Table, ArrayBlockingQueue<PreparedStatement>>();

    public StatementPool(DbConnector con, int poolSize) {
        for (Table t : ComponentRegistry.getRegistry(con).values()) {
            search.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            register.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            delete.put(t, new ArrayBlockingQueue<PreparedStatement>(50));
            for (int i = 0; i < poolSize; i++) {
                register.get(t).add(StatementFactory.createRegister(t, con));
                register.get(t).add(StatementFactory.createDelete(t, con));
            }
        }
        pools.put(con, this);
    }

    public static StatementPool getPool(DbConnector con){
        return pools.get(con);
    }

    public PreparedStatement getRegister(Table t) {
        try {
            return register.get(t).take();
        } catch (InterruptedException ex) {
            return getRegister(t);
        }
    }

    public void recycleRegister(PreparedStatement ps, Table t) {
        try {
            ps.clearParameters();
            register.get(t).add(ps);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
