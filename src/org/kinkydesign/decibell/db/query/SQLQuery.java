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
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface SQLQuery {

    /**
     * Returns the SQL-code for the underlying query. This is specific to the choise
     * of the SQL server implementation since the SQL command varies among the various
     * SQL server implementations.
     * @return
     *      SQL command as a String.
     */
    public String getSQL();

    public JTable getTable();

    public void setTable(JTable table);

    public void setColumns(Collection<? extends JTableColumn> tableColumns);

    public Collection<? extends JTableColumn> getColumns();

    public void setPropositions(ArrayList<Proposition> propositions);

    public ArrayList<Proposition> getPropositions();

    public boolean addProposition(Proposition proposition);

    public Proposition replaceProposition(int position, Proposition proposition);

    public Proposition removeProposition(Proposition proposition);

    public Proposition removeProposition(int position);

    public void setLong(JTableColumn column, long value);

    public void setShort(JTableColumn column, short value);

    public void setInt(JTableColumn column, int value);

    public void setDouble(JTableColumn column, double value);

    public void setFloat(JTableColumn column, float value);

    public void setString(JTableColumn column, String value);

    public void setNull(JTableColumn column);

    public void setUnknown(JTableColumn column);

    public void setInfinity(JTableColumn column);


}
