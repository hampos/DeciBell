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
package org.kinkydesign.decibell.db.query;

import java.util.Iterator;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.interfaces.JInsertQueryBuilder;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 * Experimental class for easy building of SQL queries.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InsertQueryBuilder implements JInsertQueryBuilder {

    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String VALUES = " VALUES ";
    private static final String QUESTION_MARK = "?";
    private static final String COMMA = " , ";
    private Table table;

    public InsertQueryBuilder() {
    }

    public InsertQueryBuilder(Table table) {
        this.table = table;
    }

    public SQLQuery insertQuery() {
        String creationSQL = INSERT_INTO + table.getTableName() + "(";
        String questionMarks = "(";
        final Iterator<TableColumn> tableColumnIterator = table.getTableColumns().iterator();
        while (tableColumnIterator.hasNext()) {
            questionMarks += QUESTION_MARK;
            creationSQL += tableColumnIterator.next().getColumnName();
            if (tableColumnIterator.hasNext()) {
                questionMarks += COMMA;
                creationSQL += COMMA;
            }
        }
        questionMarks += ")";
        creationSQL += ")" + VALUES + questionMarks;
        return new SQLQuery(creationSQL);
    }

    public SQLQuery insertQuery(String... columnNames) {
        String creationSQL = INSERT_INTO + table.getTableName() + "(";
        String questionMarks = "(";

        for (int i = 0; i < columnNames.length; i++) {
            questionMarks += QUESTION_MARK;
            creationSQL += columnNames[i];
            if (i < columnNames.length - 1) {
                questionMarks += COMMA;
                creationSQL += COMMA;
            }
        }
        questionMarks += ")";
        creationSQL += ")" + VALUES + questionMarks;
        return new SQLQuery(creationSQL);
    }

    public SQLQuery insertQuery(TableColumn... columns) {
        String creationSQL = INSERT_INTO + table.getTableName() + "(";
        String questionMarks = "(";

        for (int i = 0; i < columns.length; i++) {
            questionMarks += QUESTION_MARK;
            creationSQL += columns[i].getColumnName();

            if (i < columns.length - 1) {
                questionMarks += COMMA;
                creationSQL += COMMA;
            }
        }
        questionMarks += ")";
        creationSQL += ")" + VALUES + questionMarks;
        return new SQLQuery(creationSQL);
    }


//    public static void main(String... args) {
//        Table t = new Table("TABLE_A");
//
//        TableColumn tc1 = new TableColumn("A");
//        tc1.setColumnType(SQLType.INTEGER);
//        t.addColumn(tc1);
//
//        TableColumn tc2 = new TableColumn("B");
//        tc2.setColumnType(SQLType.INTEGER);
//        t.addColumn(tc2);
//
//        InsertQueryBuilder qb = new InsertQueryBuilder(t);
//        System.out.println(qb.insertQuery("A"));
//    }
}
