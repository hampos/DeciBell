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
import java.util.LinkedHashMap;
import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.db.interfaces.JSQLQuery;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Pantelius Sopasakius
 * @author Charalampius Chomenidius
 */
public abstract class UpdateQuery implements JSQLQuery {

    private Table table;
    protected ArrayList<Proposition> setPropositions = new ArrayList<Proposition>();
    protected ArrayList<Proposition> wherePropositions = new ArrayList<Proposition>();

    public UpdateQuery(Table table) {
        this.table = table;
        initPropositions();
    }

    public Table getTable() {
        return table;
    }

    public void setColumns(Collection<? extends TableColumn> tableColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<? extends TableColumn> getColumns() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initPropositions() {
        for (TableColumn col : table.getTableColumns()) {
            Proposition p = new Proposition();
            p.setTableColumn(col);
            p.setQualifier(Qualifier.EQUAL);
            p.setUnknown();
            setPropositions.add(p);
            if (col.isPrimaryKey()) {
                wherePropositions.add(p);
            } 
        }
    }
}
