/**
 *  Class : DerbyInsertQuery
 *  Date  : Apr 8, 2010
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
package org.kinkydesign.decibell.db.derby.query;

import java.util.Iterator;
import java.util.Map.Entry;
import org.kinkydesign.decibell.db.query.InsertQuery;
import org.kinkydesign.decibell.db.interfaces.*;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 * <p  align="justify" style="width:60%">
 * An implementation of the absrtact class {@link InsertQuery } appropriate for the
 * Derby JDBC server. Produces SQL queries which can be applied to insert data in
 * some existing table of a database. An <code>INSERT</code> statement in Derby
 * creates a row or set of such and stores them in the named table. The
 * number of values assigned in an <code>INSERT</code> statement must be the same as the number of
 * specified or implied columns.
 * Whenever you insert into a table which has generated columns, Derby calculates the
 * values of those columns. According to the Derby Reference Manual, the syntax of a
 * general <code>INSERT</code> statement is
 * <pre>INSERT INTO table-Name
 *    [ (Simple-column-Name [ , Simple-column-Name]* )
 *       Query [ ORDER BY clause ]
 *             [ result offset clause ]
 *             [ fetch first clause ]

 * </pre>
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DerbyInsertQuery extends InsertQuery {

    /**
     *
     * Consrtucts a new Derby Insert Query for a given Table.
     * @param table
     *      Table for which the Insert Query is constructed.
     */
    public DerbyInsertQuery(JTable table) {
        super(table);
    }

    public DerbyInsertQuery() {
        super();
    }

    @Override
    public String getSQL() {
        StringBuffer sql = new StringBuffer();
        sql.append(INSERT_INTO + SPACE + getTable().getTableName() + SPACE);
        StringBuffer tableCols = new StringBuffer();
        tableCols.append(LEFT_PAR);

        StringBuffer vals = new StringBuffer();
        vals.append(LEFT_PAR);
        Iterator<Entry<JTableColumn, String>> iterator = columnValuesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<JTableColumn, String> e = iterator.next();
            tableCols.append(e.getKey().getColumnName());
            if (e.getValue() == null) {
                vals.append(QUESTION_MARK);
            } else {
                vals.append(e.getValue());
            }
            if (iterator.hasNext()) {
                vals.append(COMMA);
                tableCols.append(COMMA);
            }
        }
        vals.append(RIGHT_PAR);
        tableCols.append(RIGHT_PAR);
        sql.append(tableCols);
        sql.append(SPACE + VALUES + SPACE);
        sql.append(vals);
        return sql.toString();
    }
}
