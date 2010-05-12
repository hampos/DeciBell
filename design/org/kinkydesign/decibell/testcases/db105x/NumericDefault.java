/**
 *  Class : NumericDefault
 *  Date  : May 11, 2010
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
package org.kinkydesign.decibell.testcases.db105x;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;

/**
 * When a default value is provided for a numeric field, it should be used in table
 * creation and object registration.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class NumericDefault extends Component<NumericDefault> {

    @PrimaryKey
    @Entry(defaultValue = "XYZ", notNull=true)
    private String id;

    @Entry(defaultValue = "14.25", notNull=true)
    @NumericNull(numericNullValue = "0.0")
    private double numDouble;
    
    @Entry(defaultValue = "512",notNull=true)
    @NumericNull(numericNullValue = "0.000")
    private int numInteger;

    public NumericDefault() {
    }

    public NumericDefault(String id, double numDouble, int numInteger) {
        this.id = id;
        this.numDouble = numDouble;
        this.numInteger = numInteger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getNumDouble() {
        return numDouble;
    }

    public void setNumDouble(double numDouble) {
        this.numDouble = numDouble;
    }

    public int getNumInteger() {
        return numInteger;
    }

    public void setNumInteger(int numInteger) {
        this.numInteger = numInteger;
    }
}