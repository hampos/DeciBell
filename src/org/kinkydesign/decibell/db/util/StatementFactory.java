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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.derby.query.DerbySelectQuery;
import org.kinkydesign.decibell.db.query.InsertQueryBuilder;
import org.kinkydesign.decibell.db.query.SelectQuery;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 * Factory used to prepare statements related to database queries.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class StatementFactory {

    public static PreparedStatement createSearch(Table table, DbConnector con) {
        SelectQuery a = new DerbySelectQuery(table);
        try {
            String sqlCommand = a.getSQL();
            System.out.println(sqlCommand);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM itsme.examples_User " +
                    "INNER JOIN itsme.examples_UserGroup ON itsme.examples_User.group_id = itsme.examples_UserGroup.id " +
                    " AND itsme.examples_User.group_name = itsme.examples_UserGroup.name  " +
                    "WHERE itsme.examples_User.id = ? " +
                    "AND itsme.examples_User.userName LIKE ? " +
                    "AND itsme.examples_User.age = ? " +
                    "AND itsme.examples_User.group_id = ? " +
                    "AND itsme.examples_User.group_name LIKE ? " +
                    "AND itsme.examples_User.listOfResources = ? " +
                    "AND itsme.examples_User.anotherList = ? " +
                    "AND itsme.examples_User.something = ? " +
                    "AND itsme.examples_User.kinky LIKE ? " +
                    "AND itsme.examples_User.childName = ? " +
                    "AND itsme.examples_UserGroup.id = ? " +
                    "AND itsme.examples_UserGroup.name LIKE ?");
            return ps;
        } catch (SQLException ex) {
            Logger.getLogger(StatementFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        
    }

    public static PreparedStatement createUpdate(Table table) {
        return null;

    }

    public static PreparedStatement createRegister(Table table, DbConnector con) {
        final InsertQueryBuilder builder = new InsertQueryBuilder(table);
        final String creationSQL = builder.insertQuery().toString();
        try {
            PreparedStatement ps = con.prepareStatement(creationSQL);
            return ps;
        } catch (SQLException ex) {
            throw new RuntimeException("Error while preparing the SQL statement : <" + creationSQL
                    + "> for the insertion of new data the table '"+table.getTableName()+"'", ex);
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
    public static PreparedStatement createDelete(Table table, DbConnector con) {
        String deletionSql = "DELETE FROM " + table.getTableName() + " WHERE ";
        final Iterator<TableColumn> primaryKeyColumnIterator = table.getPrimaryKeyColumns().iterator();
        while (primaryKeyColumnIterator.hasNext()) {
            deletionSql += primaryKeyColumnIterator.next().getColumnName()+"=? ";
            if (primaryKeyColumnIterator.hasNext()) {
                deletionSql += " AND ";
            }
        }
        System.out.println(deletionSql);
        try {
            PreparedStatement ps = con.prepareStatement(deletionSql);
            return ps;
        } catch (SQLException ex) {
            throw new RuntimeException("Error while preparing the SQL statement {" + deletionSql
                    + "} for deleting certain rows from the table '"+table.getTableName()+"'", ex);
        }
    }

}
