/**
 *  Class : LogicalOperator
 *  Date  : Mar 27, 2010
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


package org.kinkydesign.decibell.collections;

import org.kinkydesign.decibell.db.query.Proposition;

/**
 * Standard logical operators used in database queries. Lists of {@link Proposition propositions}
 * are seperated using some binary logical operator like {@link LogicalOperator#AND and} or
 * {@link LogicalOperator#OR or}.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum LogicalOperator {

    /**
     * The logical operator <b>AND</b> which is applied on a pair of logical propositions.
     * Let us remind the properties of this operator. Let <code>T</code> be an identically
     * true proposition and <code>F</code> a false one. Then:
     * <pre><code> T AND T = T
     * T AND F = F
     * F AND T = F
     * F AND F = F
     * </code></pre>
     */
    AND,
    /**
     * The standard logical operator <b>OR</b> which is applied on a pair of logical propositions.
     * Let us remind the properties of this operator. Let <code>T</code> be an identically
     * true proposition and <code>F</code> a false one. Then:
     *
     * <pre><code> T OR T = T
     * T OR F = T
     * F OR T = T
     * F OR F = F
     * </code></pre>
     */
    OR,
    /**
     * The standard unary logical operator <b>NOT</b>.Let us remind the properties of
     * this operator. Let <code>T</code> be an identically true proposition and
     * <code>F</code> a false one. Then:
     * <pre><code> NOT T = F
     * NOT F = T
     * </code></pre>
     * The proposition <b>NOT A</b> is sometimes denoted as <b>~A</b> or <b>A'</b>.
     */
    NOT,
    /**
     * The standard exclusive OR operator.
     */
    XOR;
}