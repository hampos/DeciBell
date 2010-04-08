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
package org.kinkydesign.decibell.db.table;

import org.kinkydesign.decibell.db.interfaces.JTable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * A Table in the database is characterized by its columns. This class offers a
 * flexible tool for manipulating database tables (creating and deleting them).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class Table implements JTable {

//    private static final String CREATE_TABLE = "CREATE TABLE";
//    private static final String DROP_TABLE = "DROP TABLE";
//    private static final String UNIQUE = "UNIQUE";
//    private static final String NOT_NULL = "NOT NULL";
//    private static final String GAAId = "GENERATED ALWAYS AS IDENTITY";
//    private static final String Fk = "FOREIGN KEY";
//    private static final String Pk = "PRIMARY KEY";
//    private static final String REFERENCES = "REFERENCES";
//    private static final String ON_DEL = "ON DELETE";
//    private static final String ON_UP = "ON UPDATE";
//    private static final String SPACE = " ";
//    private static final String NEWLINE = "\n";
//    private static final String COMMA = ",";
//    private static final String LEFT_PARENTHESIS = "(";
//    private static final String RIGHT_PARENTHESIS = ")";

    private Set<TableColumn> listOfColumns = new LinkedHashSet<TableColumn>();
    private String tableName = null;
    private Set<Table> relations = new HashSet<Table>();

    /**
     * Construct a new Table object.
     */
    public Table() {
        super();
    }

    /**
     * Construct a new Table object, given its name.
     * @param tableName
     */
    public Table(String tableName) {
        this();
        if (tableName == null) 
            throw new NullPointerException("The name of a table cannot be null");
        this.tableName = tableName;
    }

    
    public Set<TableColumn> getTableColumns() {
        return listOfColumns;
    }
    
    public void setTableColumns(Set<TableColumn> tableColumns) {
        this.listOfColumns = tableColumns;
    }

    public void addColumn(TableColumn column) {
        if (column == null)
            throw new NullPointerException("You cannot add a null column");
        if (column.getColumnName() == null) 
            throw new NullPointerException("You cannot add a column without a name");
        if (column.getColumnType() == null) 
            throw new NullPointerException("Column " + column.getColumnName() + " must have an SQL type");
        this.listOfColumns.add(column);
    }

    public void removeColumn(TableColumn column) {
        this.listOfColumns.remove(column);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public abstract String getCreationSQL();

//    public String getCreationSQL() {
//        String SQL = CREATE_TABLE + SPACE + getTableName() + NEWLINE + LEFT_PARENTHESIS + SPACE;
//        Iterator<TableColumn> it = listOfColumns.iterator();
//        while (it.hasNext()) {
//            TableColumn column = it.next();
//            SQL = SQL + column.getColumnName() + SPACE + column.getColumnType().toString()+SPACE;
//
//            if (column.isUnique()) {
//                SQL += UNIQUE + SPACE;
//            }
//            if (column.isNotNull() || column.isPrimaryKey()) {
//                SQL += NOT_NULL + SPACE;
//            }
//            if (column.isAutoGenerated()) {
//                SQL += GAAId + SPACE;
//            }
//
//            if (column.hasDefault()) {
//                SQL += column.getDefaultValue();
//            }
//
//            if (column.isConstrained()) {
//                SQL += column.getConstraint() + SPACE;
//            }
//            SQL += COMMA + NEWLINE;
//        }
//        if (!getForeignKeyColumns().isEmpty()) {
//            Set<TableColumn> foreignColumns = new LinkedHashSet<TableColumn>(getForeignKeyColumns());
//            while (!foreignColumns.isEmpty()) {
//                it = foreignColumns.iterator();
//                TableColumn fk = it.next();
//                it.remove();
//                String foreignKey = Fk + SPACE + LEFT_PARENTHESIS + fk.getColumnName();
//                String references = REFERENCES + SPACE + fk.getReferenceTable() + LEFT_PARENTHESIS + fk.getReferenceColumn() + SPACE;
//                String options = ON_DEL + SPACE + fk.getOnDelete() + SPACE + ON_UP + SPACE + fk.getOnUpdate();
//                while (it.hasNext()) {
//                    TableColumn c = it.next();
//                    if (fk.getReferenceTable().equals(c.getReferenceTable())) {
//                        foreignKey += COMMA + c.getColumnName();
//                        references += COMMA + c.getReferenceColumn();
//                        it.remove();
//                    }
//                }
//                foreignKey += RIGHT_PARENTHESIS + SPACE;
//                references += RIGHT_PARENTHESIS + SPACE;
//                SQL += foreignKey + references + options + COMMA + NEWLINE;
//            }
//        }
//        SQL += Pk + SPACE + LEFT_PARENTHESIS;
//        it = getPrimaryKeyColumns().iterator();
//        while (it.hasNext()) {
//            SQL += it.next().getColumnName();
//            if (it.hasNext()) {
//                SQL += COMMA;
//            }
//        }
//        SQL += RIGHT_PARENTHESIS;
//        SQL = SQL + NEWLINE + RIGHT_PARENTHESIS;
//        System.out.println(SQL);
//        return SQL;
//    }

//    public String getDeletionSQL() {
//        String SQL = DROP_TABLE + SPACE + getTableName();
//        return SQL;
//    }

    public Set<TableColumn> getPrimaryKeyColumns() {
        Set<TableColumn> primaryKeyColumns = new LinkedHashSet<TableColumn>();
        for (TableColumn column : listOfColumns) {
            if (column.isPrimaryKey()) {
                primaryKeyColumns.add(column);
            }
        }
        return primaryKeyColumns;
    }

    public Set<TableColumn> getForeignKeyColumns() {
        Set<TableColumn> foreignKeyColumns = new LinkedHashSet<TableColumn>();
        for (TableColumn column : listOfColumns) {
            if (column.isForeignKey()) {
                foreignKeyColumns.add(column);
            }
        }
        return foreignKeyColumns;
    }

    public void addRelation(Table t) {
        relations.add(t);
    }

    public Set<Table> getRelations() {
        return relations;
    }
}
