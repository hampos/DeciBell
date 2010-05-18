/**
 *  Class : DerbyDeleteQuery
 *  Date  : 15 Apr 2010
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
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.query.DeleteQuery;
import org.kinkydesign.decibell.db.query.Proposition;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;
import org.kinkydesign.decibell.db.derby.util.DerbyInfinity;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

/**
 * <p  align="justify" style="width:60%">
 * The general structure of a </code>DELETE</code> statement in Derby is : <code>
 * DELETE FROM table-Name [[AS] correlation-Name] [WHERE clause]</code>. This class
 * creates such statements (with out making any use of the <code>'AS'</code> (optional)
 * part of the statement.
 * </p>
 * <p  align="justify" style="width:60%">
 * A <code>DELETE</code> query is an SQL query used to remove one or many rows
 * from a table according to some criteria defined here by a set of {@link Proposition propositions}.
 * Note that, in case a table column is endowed with the <code>ON DELETE CASCADE</code> property,
 * a deletion of a row in a table, might result in the automatic deletion of entries
 * in other tables.  Finally, note that a delete query deletes data entries in a
 * table but will not delete the table itself not will it affect the structure of the tables
 * by any means.
 * </p>
 * <p  align="justify" style="width:60%">
 * A DerbyDeleteQuery is an implementation of the abstract class {@link
 * DeleteQuery } appropriate for the Derby JDBC server. According to the Derby specifications
 * the type of delete statements used in DeciBell are called <code>searched deletion</code>
 * in constradiction with the <code>positioned deletion</code> which is not used here. The
 * following excerpt from the Derby reference manual is copied below for easy reference:
 * </p>
 * <p  align="justify" style="width:60%">
 * A searched delete statement depends on the table being updated, all of its
 * conglomerates (units of storage such as heaps or indexes), and any other table named
 * in the WHERE clause. A CREATE or DROP INDEX statement for the target table of a
 * prepared searched delete statement invalidates the prepared searched delete statement.
 * </p>
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 * @see DeleteQuery
 */
public class DerbyDeleteQuery extends DeleteQuery {

    /**
     * <p  align="justify" style="width:60%">
     * Constructs a new deletion query given the table for which the query is constructed.
     * The general structure of a deletion query is <code>DELETE FROM {TABLE_NAME} WHERE
     * {PROPOSITION 1} {LOGICAL OPERATOR} {PROPOSITION 2} ... </code>. The list of
     * propositions and the logical operators are defined in the sequel by means of
     * setter methods.
     * </p>
     * @param table
     *      Table for which the deletion query is constructed
     * @see DerbyDeleteQuery#setPropositions(java.util.ArrayList) set propositions
     *
     */
    public DerbyDeleteQuery(JTable table) {
        super(table);
    }

    @Override
    public String getSQL() {
        return getSQL(false);
    }

    @Override
    public String getSQL(boolean usePKonly) {
        StringBuffer sql =  new StringBuffer();
        sql.append(DELETE + SPACE + FROM + SPACE + getTable().getTableName() + SPACE
                + WHERE + SPACE);
        Iterator<Proposition> it = propositions.iterator();
        boolean shouldAND = false;
        while (it.hasNext()) {

            Proposition temp = it.next();
            if (!usePKonly || (usePKonly && temp.getTableColumn().isPrimaryKey())) {
                if (shouldAND) {
                    sql.append(LogicalOperator.AND + SPACE);
                }
                sql.append(temp + SPACE);
                shouldAND = true;
            }

        }
        return sql.toString();
    }

    public void setInfinity(JTableColumn column) {
        SQLType columnType = column.getColumnType();
        switch (columnType) {
            case INTEGER:
                setLeftInt(column, DerbyInfinity.getLeftInt());
                setRightInt(column, DerbyInfinity.getRightInt());
                break;
            case REAL:
                setLeftFloat(column, DerbyInfinity.getLeftFloat());
                setRightFloat(column, DerbyInfinity.getRightFloat());
                break;
            case DOUBLE:
                setLeftDouble(column, DerbyInfinity.getLeftDouble());
                setRightDouble(column, DerbyInfinity.getRightDouble());
                break;
            case BIGINT:
                setLeftLong(column, DerbyInfinity.getLeftLong());
                setRightLong(column, DerbyInfinity.getRightLong());
                break;
            case SMALLINT:
                setLeftShort(column, DerbyInfinity.getLeftShort());
                setRightShort(column, DerbyInfinity.getRightShort());
                break;
            default:
                setString(column, "%%");

        }
    }
}
