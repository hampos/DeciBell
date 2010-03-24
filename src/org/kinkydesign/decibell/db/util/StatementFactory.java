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
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class StatementFactory {

    public static PreparedStatement createSearch(Table table) {
        return null;
    }

    public static PreparedStatement createUpdate(Table table) {
        return null;
    }

    public static PreparedStatement createRegister(Table table, DbConnector con) {
        String creationSQL = "INSERT INTO " + table.getTableName() + "(";
        String questionMarks = "(";
        final Iterator<TableColumn> tableColumnIterator = table.getTableColumns().iterator();

        while (tableColumnIterator.hasNext()) {
            questionMarks += "?";
            creationSQL += tableColumnIterator.next().getColumnName();
            if (tableColumnIterator.hasNext()) {
                questionMarks += " , ";
                creationSQL += " , ";
            }
        }
        questionMarks += ")";
        creationSQL += ") VALUES " + questionMarks;
        System.out.println(creationSQL);
        try {
            PreparedStatement ps = con.prepareStatement(creationSQL);
            return ps;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static PreparedStatement createDelete(Table table) {
        return null;
    }
}
