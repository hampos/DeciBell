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

package org.kinkydesign.decibell.exceptions;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.PrimaryKey;

/**
 *
 * <p  align="justify" style="width:60%">
 * Any subclass of {@link Component } attached to a {@link DeciBell DeciBell Manager}
 * must be endowed by a primary key field using the annotation {@link PrimaryKey }. This
 * not beign the case, a No-Primary-Key Excpetion is thrown. When this exception is thrown
 * the database is NOT created and the user is asked to make the nessecary modifications
 * in the attached components.
 * </p>
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class NoPrimaryKeyException extends ImproperDatabaseException {

    /**
     * Creates a new instance of <code>NoPrimaryKeyException</code> without
     * detail message.
     */
    public NoPrimaryKeyException() {
    }


    /**
     * Constructs an instance of <code>NoPrimaryKeyException</code> with the
     * specified detail message.
     * @param msg 
     *      The detail message.
     */
    public NoPrimaryKeyException(String msg) {
        super(msg);
    }
}
