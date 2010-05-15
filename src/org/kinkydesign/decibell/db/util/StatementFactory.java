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
package org.kinkydesign.decibell.db.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map.Entry;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.derby.query.DerbySelectQuery;
import org.kinkydesign.decibell.db.query.SelectQuery;
import org.kinkydesign.decibell.db.derby.query.DerbyDeleteQuery;
import org.kinkydesign.decibell.db.derby.query.DerbyInsertQuery;
import org.kinkydesign.decibell.db.derby.query.DerbyUpdateQuery;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.query.DeleteQuery;
import org.kinkydesign.decibell.db.query.InsertQuery;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.query.UpdateQuery;

/**
 * Factory used to prepare statements related to database queries.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class StatementFactory {

    /**
     * Creates a prepared statement for searching in an SQL table.
     * @param table
     *      A database table.
     * @param con
     *      A {@link DbConnector } object, that is a pointer to a database connection.
     * @return
     *      PreparedStatement for searching.
     */
    public static Entry<PreparedStatement,SQLQuery> createSearch(JTable table, DbConnector con) {
        SelectQuery query = new DerbySelectQuery(table);
        try {
            PreparedStatement ps = con.prepareStatement(query.getSQL());
            Pair pair = new Pair(ps, query);
            return pair;
        } catch (SQLException ex) {
            System.out.println("buggy SQL statement: "+query.getSQL());
            throw new RuntimeException(ex);
        }       
    }

    /**
     * Creates a prepared statement for searching by primary key only in an SQL table.
     * @param table
     *      A database table.
     * @param con
     *      A {@link DbConnector } object, that is a pointer to a database connection.
     * @return
     *      PreparedStatement for searching by primary key only.
     */
    public static Entry<PreparedStatement,SQLQuery> createSearchPK(JTable table, DbConnector con) {
        SelectQuery query = new DerbySelectQuery(table);
        try {
            PreparedStatement ps = con.prepareStatement(query.getSQL(true));
            Pair pair = new Pair(ps, query);
            return pair;
        } catch (SQLException ex) {
            System.out.println("DeciBell >>> Buggy SQL statement: "+query.getSQL(true));
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a prepared statement for updating rows in an SQL table.
     * @param table
     *      A database table.
     * @param con
     *      A {@link DbConnector } object, that is a pointer to a database connection.
     * @return
     *      PreparedStatement for updating.
     */
    public static Entry<PreparedStatement,SQLQuery> createUpdate(JTable table, DbConnector con) {
        UpdateQuery query= new DerbyUpdateQuery(table);
        try {
            PreparedStatement ps = con.prepareStatement(query.getSQL());
            Pair pair = new Pair(ps, query);
            return pair;
        } catch (SQLException ex) {
            System.out.println("DeciBell >>> Buggy SQL statement: "+query.getSQL());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a prepared statement for registering rows in an SQL table.
     * @param table
     *      A database table.
     * @param con
     *      A {@link DbConnector } object, that is a pointer to a database connection.
     * @return
     *      PreparedStatement for registering.
     */
    public static Entry<PreparedStatement,SQLQuery> createRegister(JTable table, DbConnector con) {
        InsertQuery query= new DerbyInsertQuery(table);
        try {
            PreparedStatement ps = con.prepareStatement(query.getSQL());
            Pair pair = new Pair(ps, query);
            return pair;
        } catch (SQLException ex) {
            System.out.println("DeciBell >>> Buggy SQL statement: "+query.getSQL());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a prepared statement for the deletion of certain rows of an SQL table.
     * The deletion is performed with respect to the primary key(s) value(s) for a
     * row of the table
     * @param table
     *      A database table.
     * @param con
     *      A {@link DbConnector } object, that is a pointer to a database connection.
     * @return
     *      PreparedStatement for the deletion.
     */
    public static Entry<PreparedStatement,SQLQuery> createDelete(JTable table, DbConnector con) {
        DeleteQuery query= new DerbyDeleteQuery(table);
        try {
            PreparedStatement ps = con.prepareStatement(query.getSQL());
            Pair pair = new Pair(ps, query);
            return pair;
        } catch (SQLException ex) {
            System.out.println("DeciBell >>> Buggy SQL statement: "+query.getSQL());
            throw new RuntimeException(ex);
        }
    }
}
