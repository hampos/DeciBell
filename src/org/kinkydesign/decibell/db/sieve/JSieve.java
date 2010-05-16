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
package org.kinkydesign.decibell.db.sieve;

import org.kinkydesign.decibell.Component;

/**
 *
 * <p  align="justify" style="width:60%">
 * JSieve is an interface allowing users to implement their own sieves and post-process
 * the search results. The class <code>Component</code> contains the method {@link 
 * Component#search(org.kinkydesign.decibell.DeciBell, org.kinkydesign.decibell.db.sieve.JSieve) search(db,sieve)}
 * which accepts the extra parameter <code>sieve</code> which is a sieve to be used to 
 * post-filter the search results. Only sieved results will be included in the results.
 * Users can implement the method {@link JSieve#sieve(org.kinkydesign.decibell.Component) sieve}
 * of this class to build their own sieve.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JSieve<T extends Component> {

    /**
     * Method to be implemented in a user-defined sieve to control
     * search results in a more subtle way.
     * @param component
     *      Component to be sieved.
     * @return
     *      Returns <code>true</code> if the component is sieved (passes through
     *      the sieve) and <code>false</code> otherwise.
     */
    boolean sieve(T component);

}
