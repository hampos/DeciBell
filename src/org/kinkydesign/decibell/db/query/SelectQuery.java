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
import java.util.HashSet;
import java.util.Set;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class SelectQuery implements SQLQuery {

    private JTable table;
    protected ArrayList<Proposition> propositions = new ArrayList<Proposition>();
    protected ArrayList<Join> joins = new ArrayList<Join>();

    public SelectQuery(){

    }

    public SelectQuery(JTable table) {
        setTable(table);
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table){
        this.table = table;
        initPropositions(table.getTableColumns());
    }

    public void setColumns(Collection<? extends JTableColumn> tableColumns) {
        initPropositions(tableColumns);
    }

    public Collection<? extends JTableColumn> getColumns() {
        Set<JTableColumn> columns = new HashSet<JTableColumn>();
        for(Proposition p : propositions){
            columns.add(p.getTableColumn());
        }
        return columns;
    }

    public ArrayList<Join> getJoins() {
        return joins;
    }

    public void setJoins(ArrayList<Join> joins) {
        this.joins = joins;
    }

    public void addJoin(Join join) {
        joins.add(join);
    }

    public abstract String getSQL(boolean searchPKonly);

    public Proposition removeProposition(Proposition proposition) {
        return removeProposition(propositions.indexOf(proposition));
    }

    public Proposition removeProposition(int position) {
        return propositions.remove(position);
    }

    public Proposition replaceProposition(int position, Proposition proposition) {
        Proposition p = propositions.get(position);
        propositions.add(position, proposition);
        return p;
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

    private void initPropositions(Collection<? extends JTableColumn> columns) {
        for (JTableColumn tc : columns) {
            if(tc.getColumnName().equals("METACOLUMN")){
                continue;
            }
            Proposition p = new Proposition();
            p.setTableColumn(tc);
            if (tc.getColumnType().equals(SQLType.VARCHAR)
                    || tc.getColumnType().equals(SQLType.CHAR)) {
                p.setQualifier(Qualifier.LIKE);
                p.setUnknown();
                propositions.add(p);
            } else if (tc.getColumnType().equals(SQLType.BIGINT)
                    || tc.getColumnType().equals(SQLType.DECIMAL)
                    || tc.getColumnType().equals(SQLType.DOUBLE)
                    || tc.getColumnType().equals(SQLType.INTEGER)
                    || tc.getColumnType().equals(SQLType.REAL)
                    || tc.getColumnType().equals(SQLType.SMALLINT)) {
                Proposition p1 = (Proposition) p.clone();
                p.setQualifier(Qualifier.GREATER_EQUAL);
                p1.setQualifier(Qualifier.LESS_EQUAL);
                p.setUnknown();
                p1.setUnknown();
                propositions.add(p);
                propositions.add(p1);
            }
        }
    }

//    private void updatePropositions(JTable table) {
//        Proposition p;
//        for (TableColumn tc : table.getTableColumns()) {
//            boolean shouldBeAdded = true;
//            for (Join j : joins) {
//                if (j.column2column.containsValue(tc)) {
//                    shouldBeAdded = false;
//                }
//            }
//            boolean alreadyAdded = columnsAdded.contains(tc);
//            if (!alreadyAdded && shouldBeAdded) {
//                // <editor-fold defaultstate="collapsed" desc="add proposition if not added as a joint">
//                columnsAdded.add(tc);
//                p = new Proposition();
//                p.setTableColumn(tc);
//                if (tc.getColumnType().equals(SQLType.VARCHAR)
//                        || tc.getColumnType().equals(SQLType.CHAR)) {
//                    p.setQualifier(Qualifier.LIKE);
//                    p.setUnknown();
//                    propositions.add(p);
//                } else if (tc.getColumnType().equals(SQLType.BIGINT)
//                        || tc.getColumnType().equals(SQLType.DECIMAL)
//                        || tc.getColumnType().equals(SQLType.DOUBLE)
//                        || tc.getColumnType().equals(SQLType.INTEGER)
//                        || tc.getColumnType().equals(SQLType.REAL)
//                        || tc.getColumnType().equals(SQLType.SMALLINT)) {
//                    Proposition p1 = null;
//
//                    p1 = (Proposition) p.clone();
//
//
//                    p.setQualifier(Qualifier.GREATER_EQUAL);
//                    p1.setQualifier(Qualifier.LESS_EQUAL);
//                    p.setUnknown();
//                    p1.setUnknown();
//                    propositions.add(p);
//                    propositions.add(p1);
//                }// </editor-fold>
//            }
//        }
//        Iterator<Table> remoteTables = table.getReferencedTables().iterator();
//        while (remoteTables.hasNext()) {
//            JTable tt = remoteTables.next();
//            updatePropositions(tt);
//        }
//    }

    public void setString(JTableColumn column, String stringValue) {
        if (!(column.getColumnType().equals(SQLType.VARCHAR)
                || column.getColumnType().equals(SQLType.CHAR))) {
            throw new IllegalArgumentException("Tried to assign String value to non-String"
                    + " Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setString(stringValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setLong(JTableColumn column, long longValue) {
        if (!column.getColumnType().equals(SQLType.BIGINT)) {
            throw new IllegalArgumentException("Tried to assign Long value to non-Long Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setLong(longValue);
            }
        }
    }

    public void setLeftLong(JTableColumn column, long longValue) {
        if (!column.getColumnType().equals(SQLType.BIGINT)) {
            throw new IllegalArgumentException("Tried to assign Long value to non-Long Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.GREATER_EQUAL)) {
                p.setLong(longValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setRightLong(JTableColumn column, long longValue) {
        if (!column.getColumnType().equals(SQLType.BIGINT)) {
            throw new IllegalArgumentException("Tried to assign Long value to non-Long Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.LESS_EQUAL)) {
                p.setLong(longValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setShort(JTableColumn column, short shortValue) {
        if (!column.getColumnType().equals(SQLType.SMALLINT)) {
            throw new IllegalArgumentException("Tried to assign Short value to non-Short Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setShort(shortValue);
            }
        }
    }

    public void setLeftShort(JTableColumn column, short shortValue) {
        if (!column.getColumnType().equals(SQLType.SMALLINT)) {
            throw new IllegalArgumentException("Tried to assign Short value to non-Short Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.GREATER_EQUAL)) {
                p.setShort(shortValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setRightShort(JTableColumn column, short shortValue) {
        if (!column.getColumnType().equals(SQLType.SMALLINT)) {
            throw new IllegalArgumentException("Tried to assign Short value to non-Short Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.LESS_EQUAL)) {
                p.setShort(shortValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setInt(JTableColumn column, int integerValue) {
        if (!(column.getColumnType().equals(SQLType.INTEGER)
                || column.getColumnType().equals(SQLType.BIGINT))) {
            throw new IllegalArgumentException("Tried to assign Integer value to non-Integer/Long Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setInt(integerValue);
            }
        }
    }

    public void setLeftInt(JTableColumn column, int integerValue) {
        if (!(column.getColumnType().equals(SQLType.INTEGER)
                || column.getColumnType().equals(SQLType.BIGINT))) {
            throw new IllegalArgumentException("Tried to assign Integer value to non-Integer/Long Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.GREATER_EQUAL)) {
                p.setInt(integerValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setRightInt(JTableColumn column, int integerValue) {
        if (!(column.getColumnType().equals(SQLType.INTEGER)
                || column.getColumnType().equals(SQLType.BIGINT))) {
            throw new IllegalArgumentException("Tried to assign Integer value to non-Integer/Long Column. "
                    + "Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.LESS_EQUAL)) {
                p.setInt(integerValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName() + " "
                + "does not belong to any proposition.");
    }

    public void setDouble(JTableColumn column, double doubleValue) {
        if (!(column.getColumnType().equals(SQLType.DOUBLE)
                || column.getColumnType().equals(SQLType.DECIMAL)
                || column.getColumnType().equals(SQLType.REAL))) {
            throw new IllegalArgumentException("Tried to assign Double value to"
                    + " non-Double Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setDouble(doubleValue);
            }
        }
    }

    public void setLeftDouble(JTableColumn column, double doubleValue) {
        if (!(column.getColumnType().equals(SQLType.DOUBLE)
                || column.getColumnType().equals(SQLType.DECIMAL)
                || column.getColumnType().equals(SQLType.REAL))) {
            throw new IllegalArgumentException("Tried to assign Double value to"
                    + " non-Double Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.GREATER_EQUAL)) {
                p.setDouble(doubleValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName()
                + " does not belong to any proposition.");
    }

    public void setRightDouble(JTableColumn column, double doubleValue) {
        if (!(column.getColumnType().equals(SQLType.DOUBLE)
                || column.getColumnType().equals(SQLType.DECIMAL)
                || column.getColumnType().equals(SQLType.REAL))) {
            throw new IllegalArgumentException("Tried to assign Double value to "
                    + "non-Double Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.LESS_EQUAL)) {
                p.setDouble(doubleValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName()
                + " does not belong to any proposition.");
    }
    
    public void setFloat(JTableColumn column, float floatValue) {
        if (!(column.getColumnType().equals(SQLType.DOUBLE)
                || column.getColumnType().equals(SQLType.DECIMAL)
                || column.getColumnType().equals(SQLType.REAL))) {
            throw new IllegalArgumentException("Tried to assign Float value to"
                    + " non-Float Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setFloat(floatValue);
            }
        }
    }

    public void setLeftFloat(JTableColumn column, float floatValue) {
        if (!(column.getColumnType().equals(SQLType.DOUBLE)
                || column.getColumnType().equals(SQLType.DECIMAL)
                || column.getColumnType().equals(SQLType.REAL))) {
            throw new IllegalArgumentException("Tried to assign Float value to"
                    + " non-Float Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.GREATER_EQUAL)) {
                p.setFloat(floatValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName()
                + " does not belong to any proposition.");
    }

    public void setRightFloat(JTableColumn column, float floatValue) {
        if (!(column.getColumnType().equals(SQLType.DOUBLE)
                || column.getColumnType().equals(SQLType.DECIMAL)
                || column.getColumnType().equals(SQLType.REAL))) {
            throw new IllegalArgumentException("Tried to assign Float value to "
                    + "non-Float Column. Column Type: " + column.getColumnType().toString());
        }
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column) && p.getQualifier().equals(Qualifier.LESS_EQUAL)) {
                p.setFloat(floatValue);
                return;
            }
        }
        throw new IllegalArgumentException("Column: " + column.getFullName()
                + " does not belong to any proposition.");
    }

    public void setNull(JTableColumn column) {
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setNull();
                p.setQualifier(Qualifier.IS);
            }
        }
    }

    public void setUnknown(JTableColumn column) {
        for (Proposition p : propositions) {
            if (p.getTableColumn().equals(column)) {
                p.setUnknown();
                p.setQualifier(Qualifier.EQUAL);
            }
        }
    }

    public abstract void setInfinity(JTableColumn column);

    
}
