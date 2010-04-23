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
package org.kinkydesign.decibell.db.query;

import java.util.HashMap;
import java.util.Map;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.interfaces.JTable;

/**
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class Join {

    protected Map<TableColumn, TableColumn> column2column = new HashMap<TableColumn, TableColumn>();

    public enum JOIN_TYPE {

        INNER,
        OUTER,
        LEFT,
        RIGHT;
    }

    public Join() {
    }
    private JOIN_TYPE joinType;

    public JOIN_TYPE getJoinType() {
        return joinType;
    }

    public void setJoinType(JOIN_TYPE joinType) {
        this.joinType = joinType;
    }

    public JTable getBaseTable() {
        if (column2column.isEmpty()) {
            return null;
        }
        return column2column.keySet().iterator().next().getMasterTable();
    }

    public JTable getRemoteTable() {
        if (column2column.isEmpty()) {
            return null;
        }
        return column2column.values().iterator().next().getMasterTable();
    }

    public abstract String getSQL();

    public void addColumns(TableColumn baseColumn, TableColumn remoteColumn) {
        if (!column2column.isEmpty()) {
            if (!remoteColumn.getMasterTable().equals(this.getRemoteTable())) {
                throw new IllegalArgumentException("The remote column you provided"
                        + " is not consistent with this JOIN object. Remote table conflict!\n"
                        + "Existing join :\n"
                        + " Master table = " + getBaseTable().getTableName() + "\n"
                        + " Remote table = " + getRemoteTable().getTableName() + "\n"
                        + "You provided :\n"
                        + " Master table = " + baseColumn.getMasterTable().getTableName() + "\n"
                        + " Remote table = " + remoteColumn.getMasterTable().getTableName() + " .");
            }
        }
        this.column2column.put(baseColumn, remoteColumn);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !this.getClass().equals(obj.getClass())){
            return false;
        }
        Join other = (Join) obj;
        return (other.getRemoteTable().equals(getRemoteTable()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.column2column != null ? this.column2column.hashCode() : 0);
        hash = 97 * hash + (this.joinType != null ? this.joinType.hashCode() : 0);
        return hash;
    }
}
