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
package org.kinkydesign.decibell.db.derby.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.db.query.Join;
import org.kinkydesign.decibell.db.TableColumn;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 * 
 * <p  align="justify" style="width:60%">
 * DerbyJoin is an auxiliary class which aims to facilitate the construction of
 * the <code>JOIN</code> part of a <code>SELECT</code> statement. Different types of
 * <code>JOIN</code> statements (inner, outer, right, left) can be combined together
 * in a DerbyJoin.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Join
 * @see DerbySelectQuery
 */
public class DerbyJoin extends Join {

    /**
     * Correspondence between {@link JOIN_TYPE join types} (Enumeration in {@link Join}) and
     * Derby Keywords for this type of JOIN.
     */
    private static Map<JOIN_TYPE, String> types;
    static{
        types = new HashMap<JOIN_TYPE, String>();
        types.put(JOIN_TYPE.INNER, INNER_JOIN);
        types.put(JOIN_TYPE.OUTER, OUTER_JOIN);
        types.put(JOIN_TYPE.LEFT, LEFT_JOIN);
        types.put(JOIN_TYPE.RIGHT, RIGHT_JOIN);        
    }

    /**
     *
     * Constructs a new DerbyJoin object. This constructor does not require any
     * user input/parameters.
     */
    public DerbyJoin() {
        super();
    }

    @Override
    public String getSQL() {
        String sql = types.get(getJoinType()) + SPACE + getRemoteTable().getTableName() +
                SPACE + ON + SPACE;
        Iterator<Entry<TableColumn, TableColumn>> it = column2column.entrySet().iterator();
        while (it.hasNext()){
            Entry<TableColumn, TableColumn> e = it.next();
            sql += e.getKey().getFullName() + Qualifier.EQUAL + e.getValue().getFullName() + SPACE;
            if (it.hasNext()){
                sql += LogicalOperator.AND + SPACE;
            }
        }
        return sql;
    }


}
