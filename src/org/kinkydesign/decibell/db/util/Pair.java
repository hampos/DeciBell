/**
 *  Class : Pair
 *  Date  : 17 Απρ 2010
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

package org.kinkydesign.decibell.db.util;

import java.util.Map.Entry;

/**
 * A simple pair implementation of the <code>java.util.Map.Entry</code> interface.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class Pair<Key,Value> implements Entry<Key,Value> {
    private Key key;
    private Value value;

    /**
     * Constructs a new Key-Value pair.
     * @param key a new Key
     * @param value a Value associated with the Key.
     */
    public Pair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the Key of the pair.
     * @return the Key of the pair.
     */
    public Key getKey() {
        return key;
    }

    /**
     * Returns the Value of the pair.
     * @return the Value of the pair.
     */
    public Value getValue() {
        return value;
    }

    /**
     * Associates a new Value with the pair's Key.
     * @param value a new Value to be associated with the pair's Key.
     * @return The old Value associated with the pair's Key.
     */
    public Value setValue(Value value) {
        Value oldvalue = this.value;
        this.value = value;
        return oldvalue;
    }

}
