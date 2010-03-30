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
package org.kinkydesign.decibell.collections;

import org.kinkydesign.decibell.db.query.Proposition;

/**
 * An enumeration of all available binary relations like &lt;= or &gt;=. These are
 * used within query generation and are part of those. These qualifiers play an essential
 * role in select and update queries as they are used to identify subsets of data in
 * the database. As an example, consider of a select query like <code>SELECT * FROM
 * TABLE_A WHERE A = 1 AND B &lt;= 3 AND C IS NOT NULL</code>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum Qualifier {

    EQUAL("="),
    NOT_EQUAL("<>"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_EQUAL(">="),
    LESS_EQUAL("<="),
    IS("IS"),
    IS_NOT("IS NOT");

    private String qualifier = "";

    /**
     * Private constructor of this enumeration for the creation of qualifiers given
     * their string representation/symbol.
     * @param qualifier
     */
    private Qualifier(String qualifier){
        this.qualifier = qualifier;
    }

    /**
     * The symbol corresponding to the qualifier. For example:
     * <pre><code>Qualifier qualifier = Qualifier.EQUAL;
     * System.out.println(qualifier);
     * </code></pre>
     * This will return <code>'='</code>.
     * @return
     *      The symbol of the qualifier (as a String).
     */
    @Override
    public String toString() {
        return this.qualifier;
    }



}