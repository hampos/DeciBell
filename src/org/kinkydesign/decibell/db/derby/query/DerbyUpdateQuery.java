/**
 *  Class : DerbyUpdateQuery
 *  Date  : April 15, 2010
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
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.UpdateQuery;
import org.kinkydesign.decibell.db.interfaces.JTable;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 * <p  align="justify" style="width:60%">
 * <code>UPDATE</code> statements for the Derby JDBC server. DerbyUpdate Query is
 * an implementation of the abstract class {@link UpdateQuery } intended to be used
 * for the Derby database server. This class produced what is refered in the <em>Derby
 * Reference Manual</em> as the <code>searched update</code> whose general syntax is:
 * <pre>
 * UPDATE table-Name [[AS] correlation-Name]
 *   SET column-Name = Value
 *   [ , column-Name = Value} ]*
 *   [WHERE clause] |
 * </pre>
 * </p>
 * @author Charalampos Chomenides
 * @author Pantelius Sopasakius
 * @see UpdateQuery Abstract Class UpdateQuery
 * @see DerbyInsertQuery 
 */
public class DerbyUpdateQuery extends UpdateQuery {

    public DerbyUpdateQuery(JTable table) {
        super(table);
    }

    public String getSQL() {
        return getSQL(false);
    }

    public String getSQL(boolean usePKonly) {
        String sql = UPDATE + SPACE + getTable().getTableName() + SPACE + SET + SPACE;
        Iterator<Proposition> it = setPropositions.iterator();
        while (it.hasNext()) {
            sql += it.next();
            if (it.hasNext()) {
                sql += SPACE + COMMA + SPACE;
            }
        }

        sql += SPACE + WHERE + SPACE;
        it = wherePropositions.iterator();
        while (it.hasNext()) {
            sql += it.next();
            if (it.hasNext()) {
                sql += SPACE + LogicalOperator.AND + SPACE;
            }
        }
        return sql;
    }
}
