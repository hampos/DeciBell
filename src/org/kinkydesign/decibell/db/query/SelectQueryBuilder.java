/**
 *
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

import org.kinkydesign.decibell.collections.Qualifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.interfaces.JSelectQueryBuilder;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SelectQueryBuilder implements JSelectQueryBuilder {

    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String INNER_JOIN = "INNER JOIN";
    private static final String ON = "ON";
    private static final String COMMA = ",";
    private static final String DOT = ".";
    private static final String DOT_STAR = ".*";
    private static final String WHERE = "WHERE";
    private static final String SPACE = " ";
    /**
     *
     * Name of the base table.
     */
    private Table baseTable;
    
    /**
     *
     * Secondary tables. A map from a secondary table to its corresponding column
     * on which the inner join takes place.
     */
    private LinkedHashMap<Table, TableColumn[]> secondaryMap_2_onColumn =
            new LinkedHashMap<Table, TableColumn[]>();

    /**
     *
     * Consruct a new {@link SelectQueryBuilder select query builder} defining the
     * base table on which the selection is performed. When using this constructor
     * the produced query will refer only to one table.
     * @param table
     *      the main table on which the <code>SELECT</code> is performed.
     * @throws NullPointerException
     *      In case the provided table is <code>null</code> or the name of this table
     *      is <code>null</code>
     */
    public SelectQueryBuilder(Table table) throws NullPointerException {
        if (table == null) {
            throw new NullPointerException("null table provided to the constructor of SelectQueryBuilder");
        }
        if (table.getTableName() == null) {
            throw new NullPointerException("You provided a table with name=null");
        }
        this.baseTable = table;
    }

    

    public void attach(Table otherTable, TableColumn localColumn, TableColumn remoteColumn) throws IllegalArgumentException {
        this.secondaryMap_2_onColumn.put(otherTable, new TableColumn[]{localColumn, remoteColumn});
    }

    public SQLQuery selectQuery() {
        String columnsToSelect = "";
        Iterator<TableColumn> baseColumns = baseTable.getTableColumns().iterator();
        while(baseColumns.hasNext()){
            columnsToSelect += baseTable.getTableName() + DOT + baseColumns.next().getColumnName() + SPACE;
                    if (baseColumns.hasNext() )
                        columnsToSelect += COMMA + SPACE;
        }

        String selectSQL = SELECT + SPACE + baseTable.getTableName() + DOT_STAR + SPACE + FROM + SPACE + baseTable.getTableName() + SPACE;
        if (secondaryMap_2_onColumn.size() > 0) {
            selectSQL += INNER_JOIN + SPACE;
            Iterator<Entry<Table, TableColumn[]>> it = secondaryMap_2_onColumn.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Table, TableColumn[]> entry = it.next();
                selectSQL += entry.getKey().getTableName() + SPACE + ON + SPACE + entry.getKey().getTableName() + DOT + entry.getValue()[1].getColumnName() + SPACE
                        + SPACE + Qualifier.EQUAL + SPACE + baseTable.getTableName() + DOT + entry.getValue()[0].getColumnName() + SPACE;
            }
        }
        return new SQLQuery(selectSQL);
    }

    public SQLQuery selectQuery(ArrayList<Proposition> propositions) {
        if (propositions.size() == 0) {
            return selectQuery();
        }
        return selectQuery(propositions, LogicalOperator.AND);
    }

    public SQLQuery selectQuery(ArrayList<Proposition> propositions, LogicalOperator logicalOperator) {
        if (propositions.size() == 0) {
            return selectQuery();
        }
        String selectSQL = selectQuery().toString();
        selectSQL += WHERE + SPACE;
        final Iterator<Proposition> it = propositions.iterator();
        while (it.hasNext()) {
            selectSQL += it.next().toString();
            if (it.hasNext()) {
                selectSQL += SPACE + logicalOperator + SPACE;
            }
        }
        return new SQLQuery(selectSQL);
    }

    public SQLQuery selectQuery(ArrayList<Proposition> propositions, ArrayList<LogicalOperator> logicalOperators)
            throws IllegalArgumentException {
        /* check provided data : */
        checkData:{
            try {
                assert (propositions.size() == logicalOperators.size() + 1);
            } catch (AssertionError er) {
                throw new IllegalArgumentException("Illegal sizes. logical operators have to be one less than the number of propositions.");
            }
            if (propositions.size() == 0)  return selectQuery();
        }
        String selectSQL = selectQuery().toString();
        selectSQL += WHERE + SPACE;
        final Iterator<Proposition> it = propositions.iterator();
        final Iterator<LogicalOperator> it_logical = logicalOperators.iterator();
        while (it.hasNext()) {
            selectSQL += it.next().toString();
            if (it.hasNext()) {
                selectSQL += SPACE + it_logical.next() + SPACE;
            }
        }
        return new SQLQuery(selectSQL);
    }

    
    public SQLQuery easySearchQueryEquality() {
        Set<TableColumn> columnSet = baseTable.getTableColumns();
        Set<Table> secondaryTableSet = secondaryMap_2_onColumn.keySet();
        
        ArrayList<Proposition> propositions = new ArrayList<Proposition>();
        Proposition proposition = new Proposition();
        for (TableColumn tableColumn : columnSet){
            proposition.setTableColumn(tableColumn);
            proposition.setQualifier(Qualifier.EQUAL);
            proposition.setUnknown();
            propositions.add(proposition);
            proposition = new Proposition();
        }
        return selectQuery(propositions);
    }

    

//    public static void main(String... args){
//        TableColumn userNameCol = new TableColumn("userName");
//        userNameCol.setColumnType(SQLType.VARCHAR);
//
//        TableColumn passwordCol = new TableColumn("PassWord");
//        passwordCol.setColumnType(SQLType.LONG_VARCHAR);
//
//        TableColumn groupCol = new TableColumn("group");
//        groupCol.setColumnType(SQLType.LONG_VARCHAR);
//
//        TableColumn groupNameCol = new TableColumn("GroupName");
//        groupNameCol.setColumnType(SQLType.VARCHAR);
//
//        TableColumn levelCol = new TableColumn("Level");
//        levelCol.setColumnType(SQLType.VARCHAR);
//
//        Table users = new Table("USERS");
//        users.addColumn(userNameCol);
//        users.addColumn(passwordCol);
//
//        Table userGroups = new Table("USER_GROUPS");
//        userGroups.addColumn(groupNameCol);
//        userGroups.addColumn(levelCol);
//
//        JSelectQueryBuilder builder = new SelectQueryBuilder(users);
//        builder.attach(userGroups, groupCol, groupNameCol);
//
//        System.out.println(builder.easySearchQueryEquality());
//    }


}
