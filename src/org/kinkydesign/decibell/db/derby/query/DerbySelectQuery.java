/**
 *  Class : DerbySelectQuery
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

import java.util.ArrayList;
import java.util.Iterator;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.derby.util.DerbyInfinity;
import org.kinkydesign.decibell.db.query.Join;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SelectQuery;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 *
 * <p  align="justify" style="width:60%">
 * DerbySelectQuery produces a <code>SELECT</code> type query for the Derby JDBC
 * server. Simple select query are produced optionally endowed with a <code>JOIN</code> part.
 * The <code>SELECT</code> query is applied on a table which is specified by the method
 * {@link SelectQuery#setTable(org.kinkydesign.decibell.db.interfaces.JTable) setTable}.
 * You can provide a list of {@link Join Joins} using the methods
 * {@link SelectQuery#setJoins(java.util.ArrayList) setJoins}
 * and {@link SelectQuery#addJoin(org.kinkydesign.decibell.db.query.Join) addJoin} (see documentation
 * there in for more information).
 * </p>
 *
 * <p  align="justify" style="width:60%">
 * The general structure of a <code>SELECT</code> query in Derby is:
 * <blockquote>
 * <pre>
 * SELECT [LIST OF COLUMNS] FROM [TABLE] WHERE [LIST OF PROPOSITIONS] [JOIN STATEMENT]
 * </pre></blockquote>
 * </p>
 *
 * <p  align="justify" style="width:60%">
 * <code>UNION</code>, <code>INTERSECT</code>, and <code>EXCEPT</code> and <code>FETCH</code> 
 * expressions are not supported for the time but will be added in some later version.
 * </p>
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Join
 * @see DerbyJoin
 */
public class DerbySelectQuery extends SelectQuery {

    /**
     * Construct a new DerbySelectQuery Object for a given table.
     * @param table
     *      The table on which the Derby Select Query is built.
     */
    public DerbySelectQuery(JTable table) {
        super(table);
    }

    public String getSQL() {
        return getSQL(false);
    }


    public String getSQL(boolean searchPKonly) {
        JTable table = getTable();
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT + SPACE + STAR + SPACE + FROM + SPACE + table.getTableName() + SPACE);

        for (int i=joins.size()-1;i>=0;i--) {
            sql.append(joins.get(i).getSQL() + SPACE);
        }
        for (Join j : joins){
            sql.append(j.getSQL() + SPACE);
        }

        if (searchPKonly) {
            ArrayList<Proposition> props = new ArrayList<Proposition>();
            for (JTableColumn c : table.getPrimaryKeyColumns()) {
                Proposition p = new Proposition();
                p.setTableColumn(c);
                p.setQualifier(Qualifier.EQUAL);
                p.setUnknown();
                props.add(p);
            }
            setPropositions(props);
        }
        sql.append(WHERE + SPACE);

        Iterator<Proposition> propIter = propositions.iterator();
        while (propIter.hasNext()) {
            sql.append(propIter.next().toString());
            if (propIter.hasNext()) {
                sql.append(SPACE + LogicalOperator.AND + SPACE);
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
                setString(column, DOUBLE_PERCENT);

        }
    }


}
