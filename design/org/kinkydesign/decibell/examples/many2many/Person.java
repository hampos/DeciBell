/**
 *  Class : Person
 *  Date  : May 1, 2010
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
package org.kinkydesign.decibell.examples.many2many;

import java.util.Collection;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;


/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Person extends Component<Person>{

    @PrimaryKey
    @NumericNull(numericNullValue="-1")
    public int id = -1;

    @Entry
    public String x = "sdfk";

    /**
     * <p  align="justify" style="width:60%">
     * This field uses the generic type <code>Collection</code> in java and
     * maps to a many-to-many relation in the generated database. If you supply
     * this field with a value which uses a certain implementation of the <code>Collection</code>
     * interface and you retrieve the registered object from the database then its
     * included collection will be of the same type as the one you registered. For example,
     * if you register a LinkedHashSet, the petList will be of that type once you
     * retrieve it using the method {@link Component#search(org.kinkydesign.decibell.DeciBell) search(db)}
     * </p>
     */
    @ForeignKey
    public Collection<Pet> petList;

    /**
     * <p  align="justify" style="width:60%">
     * Many lists can be included in a subclass of components. Note that the
     * annotation @{@link ForeignKey } is used to annotate collections/lists/sets.
     * </p>
     */
    @ForeignKey
    public Collection<Pet> otherList;

    
}
