/**
 *  Class : DerbyRelationalTable
 *  Date  : 23 Απρ 2010
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

package org.kinkydesign.decibell.db.derby;

import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import java.lang.reflect.Field;
import org.kinkydesign.decibell.db.interfaces.JTable;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DerbyRelationalTable extends DerbyTable implements JRelationalTable {

    private Field onField = null;

    private JTable masterTable = null;

    public DerbyRelationalTable() {
        super();
    }

    /**
     *
     * @param onField
     *      the {@link java.lang.reflect.Field } on which the relational table
     *      is created.
     * @param masterTable
     *      the Table holding the onField {@link java.lang.reflect.Field }
     */
    public DerbyRelationalTable(Field onField, JTable masterTable) {
        super();
        this.onField = onField;
        this.masterTable = masterTable;
    }

    public Field getOnField() {
        return onField;
    }

    public void setOnField(Field onField) {
        this.onField = onField;
    }

    public JTable getMasterTable() {
        return masterTable;
    }

    public void setMasterTable(JTable masterTable) {
        this.masterTable = masterTable;
    }






}
