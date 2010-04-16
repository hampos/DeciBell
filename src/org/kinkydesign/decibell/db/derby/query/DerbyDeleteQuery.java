/**
 *  Class : DerbyDeleteQuery
 *  Date  : 15 Απρ 2010
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
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.derby.DerbyTable;
import org.kinkydesign.decibell.db.query.DeleteQuery;
import org.kinkydesign.decibell.db.query.Proposition;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DerbyDeleteQuery extends DeleteQuery {

    public DerbyDeleteQuery(Table table) {
        super(table);
    }

    @Override
    public String getSQL() {
        return getSQL(true);
    }

    @Override
    public String getSQL(boolean usePKonly) {
        String sql = DELETE + SPACE + FROM + SPACE + getTable().getTableName() + SPACE
                + WHERE + SPACE;
        Iterator<Proposition> it = propositions.iterator();
        boolean shouldAND = false;
        while (it.hasNext()) {
            
            Proposition temp = it.next();
            if (!usePKonly || (usePKonly && temp.getTableColumn().isPrimaryKey())) {
                if (shouldAND){
                sql += LogicalOperator.AND + SPACE;
            }
                sql += temp + SPACE;
                shouldAND = true;
            }

        }
        return sql;
    }

    public static void main(String... args) {
        Table t3 = new DerbyTable();
        t3.setTableName("C");
        TableColumn c1 = new TableColumn("f");
        c1.setColumnType(SQLType.INTEGER);
        c1.setPrimaryKey(true, false);
        t3.addColumn(c1);
        TableColumn c2 = new TableColumn("k");
        c2.setColumnType(SQLType.VARCHAR);
        c2.setPrimaryKey(true, true);
        t3.addColumn(c2);


        Table t2 = new DerbyTable();
        t2.setTableName("B");
        TableColumn b1 = new TableColumn("i");
        b1.setForeignKey(t3, c1, OnModification.CASCADE, OnModification.NO_ACTION);
        b1.setColumnType(SQLType.INTEGER);
        b1.setPrimaryKey(true, false);
        t2.addColumn(b1);
        TableColumn b2 = new TableColumn("j");
        b2.setColumnType(SQLType.VARCHAR);
        t2.addColumn(b2);
        Table t = new DerbyTable();
        t.setTableName("A");

        TableColumn tc1 = new TableColumn("x");
        tc1.setColumnType(SQLType.INTEGER);
        tc1.setForeignKey(t2, b1, OnModification.CASCADE, OnModification.NO_ACTION);
        t.addColumn(tc1);

        TableColumn tc2 = new TableColumn("y");
        tc2.setColumnType(SQLType.VARCHAR);
        tc2.setForeignKey(t2, b1, OnModification.CASCADE, OnModification.NO_ACTION);
        t.addColumn(tc2);

        TableColumn tc3 = new TableColumn("z");
        tc3.setColumnType(SQLType.INTEGER);
        tc3.setForeignKey(t3, c1, OnModification.CASCADE, OnModification.CASCADE);
        tc3.setPrimaryKey(true, true);
        t.addColumn(tc3);

        TableColumn tc4 = new TableColumn("w");
        tc4.setColumnType(SQLType.INTEGER);
        tc4.setForeignKey(t3, c2, OnModification.CASCADE, OnModification.CASCADE);
        tc4.setPrimaryKey(true, true);
        t.addColumn(tc4);

        DerbyDeleteQuery DD = new DerbyDeleteQuery(t);
        System.out.println(DD.getSQL());
    }
}
