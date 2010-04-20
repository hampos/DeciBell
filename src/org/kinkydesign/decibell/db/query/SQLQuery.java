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

import java.util.ArrayList;
import java.util.Collection;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface SQLQuery {

    public String getSQL();

    public Table getTable();

    public void setTable(Table table);

    public void setColumns(Collection<? extends TableColumn> tableColumns);

    public Collection<? extends TableColumn> getColumns();

    public void setPropositions(ArrayList<Proposition> propositions);

    public ArrayList<Proposition> getPropositions();

    public boolean addProposition(Proposition proposition);

    public Proposition replaceProposition(int position, Proposition proposition);

    public Proposition removeProposition(Proposition proposition);

    public Proposition removeProposition(int position);

    public void setLong(TableColumn column, long value);

    public void setInt(TableColumn column, int value);

    public void setDouble(TableColumn column, double value);

    public void setString(TableColumn column, String value);

    public void setNull(TableColumn column);

    public void setUnknown(TableColumn column);

    public void setInfinity(TableColumn column);


}
