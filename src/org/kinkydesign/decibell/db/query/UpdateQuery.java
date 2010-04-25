/**
 *  Class : UpdateQuery
 *  Date  : April 15, 2010
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
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

/**
 *
 * @author Pantelius Sopasakius
 * @author Charalampius Chomenidius
 */
public abstract class UpdateQuery implements SQLQuery {

    private JTable table;
    protected ArrayList<Proposition> setPropositions = new ArrayList<Proposition>();
    protected ArrayList<Proposition> wherePropositions = new ArrayList<Proposition>();

    public UpdateQuery() {
    }

    public UpdateQuery(JTable table) {
        setTable(table);
    }

    public abstract String getSQL(boolean usePKonly);

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
        initPropositions(table.getTableColumns(), table.getTableColumns());
    }

    public void setColumns(Collection<? extends JTableColumn> tableColumns) {
        initPropositions(tableColumns, tableColumns);
    }

    public void setColumns(Collection<? extends JTableColumn> setColumns,
            Collection<? extends JTableColumn> whereColumns) {
        initPropositions(setColumns, whereColumns);
    }

    public Collection<? extends JTableColumn> getColumns() {
        Set<JTableColumn> columns = new HashSet<JTableColumn>();
        for (Proposition p : setPropositions) {
            columns.add(p.getTableColumn());
        }
        for (Proposition p : wherePropositions) {
            columns.add(p.getTableColumn());
        }
        return columns;
    }

    public void setPropositions(ArrayList<Proposition> propositions) {
        setPropositions(propositions,propositions);
    }

    public void setPropositions(ArrayList<Proposition> setPropositions,
            ArrayList<Proposition> wherePropositions) {
        this.setPropositions = setPropositions;
        this.wherePropositions = wherePropositions;
    }

    public ArrayList<Proposition> getPropositions() {
        ArrayList<Proposition> propositions = new ArrayList<Proposition>();
        propositions.addAll(setPropositions);
        propositions.addAll(wherePropositions);
        return propositions;
    }

    private void initPropositions(Collection<? extends JTableColumn> setColumns,
            Collection<? extends JTableColumn> whereColumns) {
        for (JTableColumn col : setColumns) {
            Proposition p = new Proposition();
            p.setTableColumn(col);
            p.setQualifier(Qualifier.EQUAL);
            p.setUnknown();
            setPropositions.add(p);
        }
        for (JTableColumn tc : whereColumns) {
            Proposition p = new Proposition();
            p.setTableColumn(tc);
            if (tc.getColumnType().equals(SQLType.VARCHAR)
                    || tc.getColumnType().equals(SQLType.CHAR)) {
                p.setQualifier(Qualifier.LIKE);
                p.setUnknown();
                wherePropositions.add(p);
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
                wherePropositions.add(p);
                wherePropositions.add(p1);
            }
        }
    }

    public boolean addProposition(Proposition proposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Proposition removeProposition(Proposition proposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Proposition removeProposition(int position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Proposition replaceProposition(int position, Proposition proposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDouble(JTableColumn column, double value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setInfinity(JTableColumn column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setInt(JTableColumn column, int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFloat(JTableColumn column, float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setShort(JTableColumn column, short value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLong(JTableColumn column, long value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNull(JTableColumn column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setString(JTableColumn column, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUnknown(JTableColumn column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
