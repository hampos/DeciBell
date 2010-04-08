/**
 *  Class : SelectQuery
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
package org.kinkydesign.decibell.db.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.interfaces.JSQLQuery;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class SelectQuery implements JSQLQuery{

    private Table table;
    private Collection<? extends TableColumn> tableColumns;
    protected ArrayList<Proposition> propositions = new ArrayList<Proposition>();

    public Proposition removeProposition(int index) {
        return propositions.remove(index);
    }

    public boolean addProposition(Proposition e) {
        return propositions.add(e);
    }

    public void setPropositions(ArrayList<Proposition> propositions) {
        this.propositions = propositions;
    }

    public ArrayList<Proposition> getPropositions() {
        return propositions;
    }

    public SelectQuery() {
    }

    public SelectQuery(Table table) {
        this.table = table;
        updatePropositions();
    }

    private void updatePropositions() {
        updatePropositions(table);
    }

    private void updatePropositions(Table table) {
        Proposition p;
        for (TableColumn tc : table.getTableColumns()) {
            p = new Proposition();
            p.setTableColumn(tc);
            if (tc.getColumnType().equals(SQLType.VARCHAR)
                    || tc.getColumnType().equals(SQLType.CHAR)) {
                p.setQualifier(Qualifier.LIKE);
            } else {
                p.setQualifier(Qualifier.EQUAL);
            }

            p.setUnknown();
            propositions.add(p);
        }
        Iterator<Table> remoteTables = table.getReferencedTables().iterator();
        while (remoteTables.hasNext()) {
            Table tt = remoteTables.next();
            updatePropositions(tt);
        }
    }

    public void setString(TableColumn column, String stringValue) {
        Iterator<Proposition> propos = propositions.iterator();
        Proposition p;
        while (propos.hasNext()) {
            p = propos.next();
            if (p.getTableColumn().equals(column)) {
                p.setString(stringValue);
            }
            break;
        }
    }

    public void setInt(TableColumn column, int integerValue) {
        Iterator<Proposition> propos = propositions.iterator();
        Proposition p;
        while (propos.hasNext()) {
            p = propos.next();
            if (p.getTableColumn().equals(column)) {
                p.setInt(integerValue);
            }
            break;
        }
    }

    public void setDouble(TableColumn column, double doubleValue) {
        Iterator<Proposition> propos = propositions.iterator();
        Proposition p;
        while (propos.hasNext()) {
            p = propos.next();
            if (p.getTableColumn().equals(column)) {
                p.setDouble(doubleValue);
            }
            break;
        }
    }

    public void setNull(TableColumn column) {
        Iterator<Proposition> propos = propositions.iterator();
        Proposition p;
        while (propos.hasNext()) {
            p = propos.next();
            if (p.getTableColumn().equals(column)) {
                p.setNull();
                p.setQualifier(Qualifier.IS);
            }
            break;
        }
    }

    public void setUnknown(TableColumn column) {
        Iterator<Proposition> propos = propositions.iterator();
        Proposition p;
        while (propos.hasNext()) {
            p = propos.next();
            if (p.getTableColumn().equals(column)) {
                p.setUnknown();
                p.setQualifier(Qualifier.EQUAL);
            }
            break;
        }
    }

    public Table getTable() {
        return table;
    }

    public void setColumns(Collection<? extends TableColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public Collection<? extends TableColumn> getColumns() {
        return tableColumns;
    }
}
