/**
 *  Class : DerbyKeyWord
 *  Date  : Apr 8, 2010
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
package org.kinkydesign.decibell.db.derby.util;

import org.kinkydesign.decibell.collections.Qualifier;

/**
 *
 * <p  align="justify" style="width:60%">
 * A collection of derby keywords that are used in this project. This is just a collection
 * of Strings which aims to avoid typos and stand as a global reference/dictionary of the
 * corresponding SQL language.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class DerbyKeyWord {

    /**
     * 
     * Non-breaking space character.
     */
    public static final String SPACE = " ";
    /**
     *
     * INSERT INTO
     */
    public static final String INSERT_INTO = "INSERT INTO";
    /**
     *
     * DELETE
     */
    public static final String DELETE = "DELETE";
    /**
     *
     * UPDATE
     */
    public static final String UPDATE = "UPDATE";
    /**
     *
     * COUNT
     */
    public static final String COUNT = "COUNT";
    /**
     *
     * SELECT
     */
    public static final String SELECT = "SELECT";
    /**
     *
     * ON
     */
    public static final String ON = "ON";
    /**
     *
     * FROM
     */
    public static final String FROM = "FROM";
    /**
     *
     * WHERE
     */
    public static final String WHERE = "WHERE";
    /**
     *
     * INNER JOIN
     */
    public static final String INNER_JOIN = "INNER JOIN";
    /**
     *
     * OUTER JOIN
     */
    public static final String OUTER_JOIN = "OUTER JOIN";
    /**
     *
     * LEFT OUTER JOIN
     */
    public static final String LEFT_JOIN = "LEFT OUTER JOIN";
    /**
     *
     * RIGHT OUTER JOIN
     */
    public static final String RIGHT_JOIN = "RIGHT OUTER JOIN";
    /**
     *
     * The Questionmark symbol : ?
     */
    public static final String QUESTION_MARK = "?";
    /**
     *
     * VALUES
     */
    public static final String VALUES = "VALUES";
    /**
     *
     * The left parenthesis : (
     */
    public static final String LEFT_PAR = "(";
    /**
     *
     * The right parenthesis : )
     */
    public static final String RIGHT_PAR = ")";
    /**
     *
     * The comma : ,
     */
    public static final String COMMA = ",";
    /**
     *
     * A dot : .
     */
    public static final String DOT = ".";
    /**
     *
     * A star : *
     */
    public static final String STAR = "*";
    /**
     *
     * The regular expression for a dot which should be used in the method {@link
     * String#split(java.lang.String) } instead of the simple dot character '.' .
     */
    public static final String DOT_REG = "\\.";
    /**
     *
     * A new line
     */
    public static final String NEWLINE = "\n";
    /**
     *
     * DEFAULT
     */
    public static final String DEFAULT = "DEFAULT";
    /**
     *
     * FOREIGN KEY
     */
    public static final String Fk = "FOREIGN KEY";
    /**
     *
     * PRIMARY KEY
     */
    public static final String Pk = "PRIMARY KEY";
    /**
     *
     * GENERATED ALWAYS AS IDENTITY
     */
    public static final String AUTO_GEN = "GENERATED ALWAYS AS IDENTITY";
    /**
     *
     * NOT NULL
     */
    public static final String NOT_NULL = "NOT NULL";
    /**
     *
     * UNIQUE
     */
    public static final String UNIQUE = "UNIQUE";
    /**
     *
     * CREATE TABLE
     */
    public static final String CREATE_TABLE = "CREATE TABLE";
    /**
     *
     * DROP TABLE
     */
    public static final String DROP_TABLE = "DROP TABLE";
    /**
     *
     * IN
     */
    public static final String IN = "IN";
    /**
     *
     * A single quote : '
     */
    public static final String SINGLE_QUOTE = "'";
    /**
     *
     * CONSTRAINT
     */
    public static final String CONSTRAINT = "CONSTRAINT";
    /**
     *
     * An underscore : _
     */
    public static final String UNDERSCORE = "_";
    /**
     *
     * CHECK
     */
    public static final String CHECK = "CHECK";
    /**
     *
     * REFERENCES
     */
    public static final String REFERENCES = "REFERENCES";
    /**
     *
     * SET
     */
    public static final String SET = "SET";
    /**
     *
     * The percent symbol : %
     */
    public static final String PERCENT = "%";
    /**
     *
     * Double percent symbol %%
     * @see Qualifier#LIKE like operator
     */
    public static final String DOUBLE_PERCENT = PERCENT + PERCENT;

    public static final String METACOL = "METACOLUMN";
}
