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
package org.kinkydesign.decibell.db;

import java.util.HashMap;
import org.kinkydesign.decibell.db.interfaces.JTable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

/**
 *
 * A Table in the database is characterized by its columns. This class offers a
 * flexible tool for manipulating database tables (creating and deleting them).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class Table implements JTable {

    private Set<JTableColumn> listOfColumns = new LinkedHashSet<JTableColumn>();
    private String tableName = null;
    private Set<JRelationalTable> relations = new HashSet<JRelationalTable>();

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
        if (tableName == null) {
            throw new NullPointerException("The name of a table cannot be null");
        }
        this.tableName = tableName;
    }

    public Set<JTableColumn> getTableColumns() {
        return listOfColumns;
    }

    public void setTableColumns(Set<JTableColumn> tableColumns) {
        this.listOfColumns = tableColumns;
    }

    public void addColumn(JTableColumn column) {
        if (column == null) {
            throw new NullPointerException("You cannot add a null column");
        }
        if (column.getColumnName() == null) {
            throw new NullPointerException("You cannot add a column without a name");
        }
        if (column.getColumnType() == null) {
            throw new NullPointerException("Column " + column.getColumnName() + " must have an SQL type");
        }
        column.setMasterTable(this);
        this.listOfColumns.add(column);
    }

    public void removeColumn(JTableColumn column) {
        this.listOfColumns.remove(column);
        column.setMasterTable(null);
    }

    public void setTableName(String tableName) {
        if (tableName == null) {
            throw new NullPointerException("The name of a table cannot be null");
        }
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public Set<JTableColumn> getPrimaryKeyColumns() {
        Set<JTableColumn> primaryKeyColumns = new LinkedHashSet<JTableColumn>();
        for (JTableColumn column : listOfColumns) {
            if (column.isPrimaryKey()) {
                primaryKeyColumns.add(column);
            }
        }
        return primaryKeyColumns;
    }

    public Map<JTableColumn, JTableColumn> referenceRelation() {
        Map<JTableColumn, JTableColumn> map = new HashMap<JTableColumn, JTableColumn>();
        for (JTableColumn masterColumn : getForeignKeyColumns()) {
            map.put(masterColumn, masterColumn.getReferenceColumn());
        }
        return map;
    }

    public Set<JTableColumn> getForeignKeyColumns() {
        Set<JTableColumn> foreignKeyColumns = new LinkedHashSet<JTableColumn>();
        for (JTableColumn column : listOfColumns) {
            if (column.isForeignKey()) {
                foreignKeyColumns.add(column);
            }
        }
        return foreignKeyColumns;
    }

    public Set<Set<JTableColumn>> getForeignColumnsByGroup() {
        Set<Set<JTableColumn>> groupedColumns = new HashSet<Set<JTableColumn>>();
        Set<JTableColumn> foreignColumns = getForeignKeyColumns();

        while (!foreignColumns.isEmpty()) {
            Iterator<JTableColumn> it = foreignColumns.iterator();
            JTableColumn col = it.next();
            boolean foundGroup = false;
            for (Set<JTableColumn> group : groupedColumns) {
                for (JTableColumn c : group) {
                    if (c.getReferenceTable().equals(col.getReferenceTable())) {
                        if (c.getField().equals(col.getField())) {
                            foundGroup = true;
                        } else if (col.getMasterTable() instanceof JRelationalTable && c.getMasterTable() instanceof JRelationalTable) {
                            JRelationalTable rel1 = (JRelationalTable) col.getMasterTable();
                            JRelationalTable rel2 = (JRelationalTable) c.getMasterTable();
                            if (rel1.getOnField().equals(rel2.getOnField())) {
                                foundGroup = true;
                            }
                        }
                    }
                }
                if (foundGroup) {
                    group.add(col);
                    it.remove();
                    break;
                }
            }
            if (!foundGroup) {
                Set<JTableColumn> group = new LinkedHashSet<JTableColumn>();
                group.add(col);
                groupedColumns.add(group);
                it.remove();
            }
        }
        return groupedColumns;
    }

    public Set<JTable> getReferencedTables() {
        Set<JTable> remoteTables = new HashSet<JTable>();

        for (JTableColumn remoteTableColumn : getForeignKeyColumns()) {
            remoteTables.add(remoteTableColumn.getReferenceTable());
        }
        return remoteTables;
    }

    public void addRelation(JRelationalTable t) {
        relations.add(t);
    }

    public Set<JRelationalTable> getRelations() {
        return relations;
    }

    public Set<JTableColumn> getUniqueColumns() {
       Set<JTableColumn> uniqueColumns = new LinkedHashSet<JTableColumn>();
       for (JTableColumn c : getTableColumns()){
           if (c.isUnique()){
               uniqueColumns.add(c);
           }
       }
       return uniqueColumns;
   }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        Table other = (Table) obj;
        return getTableName().equals(other.getTableName());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.tableName != null ? this.tableName.hashCode() : 0);
        return hash;
    }
}
