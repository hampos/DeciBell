/**
 *  Class : NumericNull
 *  Date  : Apr 26, 2010
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


package org.kinkydesign.decibell.annotations;

import java.lang.annotation.*;

/**
* <p align="justify" style="width:60%">
 * This DeciBell annotation is used to provide a <em>NumericNull</em> value for database
 * searching. The idea is simple. When a search command is executed on a DeciBell
 * Component DeciBell checks that component's Fields and when a Field has a <code>null</code>
 * value it gets replaced by an infinite value. (A value used to inform the database
 * that this specific field is to be ignored in the search, in the sense to perform
 * the search operation irrespective of the value of this field)
 * In that way DeciBell keeps using the same PreparedStatements and
 * greatly increasing performance even though some of the statements values have
 * to be ignored.
 * The problem comes in when Java automatically initializes all primitive numerics
 * with a 0 value (There is no meaning of <code>null</code> for primitives).
 * This makes us unable to see if the field was initialized by
 * the user or by the compiler. A new annotation then became clear.
 * NumericNull is used to define the value on which a numeric primitive is
 * considered null. The default value is -1. One can set a numeric null value
 * other than that on each DeciBell Entry. If one is to use this annotation, they must provide
 * a numeric value that they will not (or at least not expect to) use ever in their
 * database for that field; otherwise this will lead to a considerable comfusion and
 * wrong results.
 * </p>
 * @author Pantelius Sopasakius!
 * @author Charalampos Chomenides
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
public @interface NumericNull {

    public String value() default "-1";

}