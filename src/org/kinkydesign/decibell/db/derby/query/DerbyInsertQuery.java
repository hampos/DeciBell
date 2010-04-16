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
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.derby.DerbyTable;
import org.kinkydesign.decibell.db.query.InsertQuery;
import org.kinkydesign.decibell.db.query.UpdateQuery;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DerbyInsertQuery extends InsertQuery {

    public DerbyInsertQuery(Table table) {
        super(table);
    }

    public DerbyInsertQuery() {
    }



    @Override
    public String getSQL() {
        String sql = INSERT_INTO + SPACE + getTable().getTableName() + SPACE;
        String tableCols = LEFT_PAR;
        String vals = LEFT_PAR;
        Iterator<Entry<TableColumn, String>> iterator = ColumnValuesMap.entrySet().iterator();
        while (iterator.hasNext()){
            Entry<TableColumn, String> e = iterator.next();
            tableCols += e.getKey().getColumnName();
            if (e.getValue() == null){
                vals += QUESTION_MARK;
            }else{
                vals += e.getValue();
            }
            if (iterator.hasNext()){
                vals += COMMA;
                tableCols += COMMA;
            }
        }
        vals += RIGHT_PAR;
        tableCols += RIGHT_PAR;
        sql += tableCols+SPACE+VALUES+SPACE+vals;
        return sql;
    }

    public static void main(String... args){
        Table t = new DerbyTable();
        t.setTableName("MY_TABLE");

        TableColumn tc1 = new TableColumn("A");
        tc1.setColumnType(SQLType.INTEGER);
        t.addColumn(tc1);

        TableColumn tc2 = new TableColumn("B");
        tc2.setColumnType(SQLType.INTEGER);
        t.addColumn(tc2);

        InsertQuery a = new DerbyInsertQuery(t);
        a.setDouble(tc2, 3);
        a.setString(tc1, "this is a string");
        System.out.println(a.getSQL());

        UpdateQuery b = new DerbyUpdateQuery(t);
        System.out.println(b.getSQL());
    }
    
}
