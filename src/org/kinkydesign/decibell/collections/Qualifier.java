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

    /**
     *
     * Equality Relation. This operator must not be applied on <code>null</code> entities -
     * use the operator {@link Qualifier#IS is} instead.
     */
    EQUAL("="),
    /**
     *
     * Used to denote that two values are not equal. This operator must not
     * be applied on <code>null</code> entities -
     * use the operator {@link Qualifier#IS_NOT is not} instead.
     */
    NOT_EQUAL("<>"),
    /**
     *
     * Binary relation used to denote that some value is greated than and
     * <b>not equal</b> to some other according to some underlying ordering
     * operator.
     * @see Qualifier#GREATER_EQUAL greater equal
     * @see Qualifier#LESS_EQUAL less equal
     * @see Qualifier#LESS_THAN less than
     */
    GREATER_THAN(">"),
    /**
     *
     * Binary relation used to denote that some value is less than and
     * <b>not equal</b> to some other according to some underlying ordering
     * operator.
     * @see Qualifier#GREATER_EQUAL greater equal
     * @see Qualifier#GREATER_THAN greater than
     * @see Qualifier#LESS_EQUAL less equal
     */
    LESS_THAN("<"),
    /**
     *
     * Binary relation used to denote that some value is greated than and
     * or equal to some other according to some underlying ordering
     * operator.
     * @see Qualifier#LESS_EQUAL less equal
     * @see Qualifier#LESS_THAN less than
     * @see Qualifier#GREATER_THAN greater than
     */
    GREATER_EQUAL(">="),
    /**
     *
     * Binary relation used to denote that some value is less than
     * or equal to some other according to some underlying ordering
     * operator.
     * @see Qualifier#LESS_THAN less than
     * @see Qualifier#GREATER_THAN greater than
     * @see Qualifier#GREATER_EQUAL greater equal
     */
    LESS_EQUAL("<="),
    /**
     *
     * Operator used to compare any entity with a <code>null</code> one. So <code>is</code>
     * in fact stands for <code>is null</code>
     * @see Qualifier#IS_NOT is not
     */
    IS("IS"),
    /**
     *
     * Qualifier used to compare entities with the <code>null</code> entity.
     */
    IS_NOT("IS NOT"),
    /**
     *
     * A pair of string-like values belong to the <code>like</code> operator if they
     * <em>resemble</em>. The way the <code>LIKE</code> operator works relies on the
     * SQL server implementation.
     */
    LIKE("LIKE"),
    /**
     * The 'belonging' operator used to declare that a given subject belong to some
     * collection of objects.
     */
    IN("IN");

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