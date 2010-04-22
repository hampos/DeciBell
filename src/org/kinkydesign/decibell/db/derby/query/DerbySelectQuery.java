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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.derby.DerbyTable;
import org.kinkydesign.decibell.db.derby.util.DerbyInfinity;
import org.kinkydesign.decibell.db.query.Join;
import org.kinkydesign.decibell.db.query.Join.JOIN_TYPE;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SelectQuery;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DerbySelectQuery extends SelectQuery {

    public DerbySelectQuery(Table table) {
        super(table);
    }

    public String getSQL() {
        return getSQL(false);
    }

//    private void initializeJoins() {
//        Table table = getTable();
//        Map<TableColumn, TableColumn> relations = table.referenceRelation();
//        Set<Table> remoteTables = new HashSet<Table>();
//        for (TableColumn remote : relations.values()) {
//            remoteTables.add(remote.getMasterTable());
//        }
//        for (Table remoteTable : remoteTables) {
//            relations.putAll(remoteTable.referenceRelation());
//        }
//
//        Iterator<Entry<TableColumn, TableColumn>> it = relations.entrySet().iterator();
//        if (!relations.isEmpty()) {
//            while (it.hasNext()) {
//                Entry<TableColumn, TableColumn> e = it.next();
//                Join join = new DerbyJoin();
//                join.setJoinType(JOIN_TYPE.INNER);
//                join.addColumns(e.getKey(), e.getValue());
//                if (!joins.contains(join)) {
//                    joins.add(join);
//                } else {
//                    joins.get(joins.indexOf(join)).addColumns(e.getKey(), e.getValue());
//                }
//            }
//        }
//    }

    public String getSQL(boolean searchPKonly) {
        Table table = getTable();
        String sql = SELECT + SPACE + STAR + SPACE + FROM + SPACE + table.getTableName() + SPACE;

        for (int i=joins.size()-1;i>=0;i--) {
            sql += joins.get(i).getSQL() + SPACE;
        }
        for (Join j : joins){
            sql += j.getSQL() + SPACE;
        }

        if (searchPKonly) {
            ArrayList<Proposition> props = new ArrayList<Proposition>();
            for (TableColumn c : table.getPrimaryKeyColumns()) {
                Proposition p = new Proposition();
                p.setTableColumn(c);
                p.setQualifier(Qualifier.EQUAL);
                p.setUnknown();
                props.add(p);
            }
            setPropositions(props);
        }
        sql += WHERE + SPACE;

        Iterator<Proposition> propIter = propositions.iterator();
        while (propIter.hasNext()) {
            sql += propIter.next().toString();
            if (propIter.hasNext()) {
                sql += SPACE + LogicalOperator.AND + SPACE;
            }
        }
        return sql;
    }

    public void setInfinity(TableColumn column) {
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

    public static void main(String... args) {

        Table t4 = new DerbyTable();
        t4.setTableName("D");
        TableColumn d1 = new TableColumn("d1");
        d1.setColumnType(SQLType.INTEGER);
        d1.setPrimaryKey(true, false);
        t4.addColumn(d1);
        TableColumn d2 = new TableColumn("d2");
        d2.setColumnType(SQLType.VARCHAR);
        d2.setPrimaryKey(true, true);
        t4.addColumn(d2);

        Table t3 = new DerbyTable();
        t3.setTableName("C");
        TableColumn c1 = new TableColumn("f");
        c1.setColumnType(SQLType.INTEGER);
        c1.setPrimaryKey(true, false);
        c1.setForeignKey(t4, d1, OnModification.CASCADE, OnModification.CASCADE);
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
        tc1.setPrimaryKey(true, true);
        t.addColumn(tc1);

        TableColumn tc2 = new TableColumn("y");
        tc2.setColumnType(SQLType.VARCHAR);
        tc2.setForeignKey(t2, b1, OnModification.CASCADE, OnModification.NO_ACTION);
        t.addColumn(tc2);

        TableColumn tc3 = new TableColumn("z");
        tc3.setColumnType(SQLType.INTEGER);
        tc3.setForeignKey(t3, c1, OnModification.CASCADE, OnModification.CASCADE);
        t.addColumn(tc3);

        TableColumn tc4 = new TableColumn("w");
        tc4.setColumnType(SQLType.INTEGER);
        tc4.setForeignKey(t3, c2, OnModification.CASCADE, OnModification.CASCADE);
        t.addColumn(tc4);


        DerbySelectQuery a = new DerbySelectQuery(t);
        a.setLeftInt(tc1, 6);
        a.setRightInt(tc1, 5);
        a.setString(tc2, "ad");
        // a.setRightInt(tc2, 4);

        System.out.println(a.getSQL(false));
    }
}
