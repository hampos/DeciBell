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

import java.sql.Types;

/**
 *
 * <p  align="justify" style="width:60%">
 * An almost complete list of all datatypes accepted by Derby&copy;. The documentation
 * of the following enumeration elements is copied here from the Derby Manual.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see TypeMap Correspondence between java datatypes and SQL ones.
 */
public enum SQLType{

    /**
     *
     * <p  align="justify" style="width:60%">
     * Integer in SQL is the analogue of Integer in java. Lower and Higher Bounds are
     * -2147483648 and 2147483647 respectively.<code>INTEGER</code> provides 4 bytes
     * of storage for integer values.
     * </p>
     */
    INTEGER(Types.INTEGER),
    /**
     *
     * <p  align="justify" style="width:60%">
     * <code>SMALLINT</code> is the counterpart of <code>short </code>. Higher and Lower
     * limits are 32767 and -32768 respectively. <code>SMALLINT</code> provides 2 bytes of storage.
     * </p>
     */
    SMALLINT(Types.SMALLINT),
    /**
     *
     * <p  align="justify" style="width:60%">
     * <code>BIGINT</code> is like <code>Long</code> in java. It accepts values in the
     * range from -9223372036854775808 up to 9223372036854775807.
     * </p>
     */
    BIGINT(Types.BIGINT),
    /**
     *
     * <p  align="justify" style="width:60%">
     * The REAL data type provides 4 bytes of storage for numbers using IEEE floating-point
     * notation. Note that the limits in this SQL type are different from the corresponding
     * java type (<code>Float</code>).
     * </p>
     * <p  align="justify" style="width:60%">
     * REAL value ranges:<br/>
     * • Smallest REAL value: -3.402E+38<br/>
     * • Largest REAL value: 3.402E+38<br/>
     * • Smallest positive REAL value: 1.175E-37<br/>
     * • Largest negative REAL value: -1.175E-37<br/>
     * </p>
     */
    REAL(Types.REAL),
    /**
     *
     * <p  align="justify" style="width:60%">
     * The DOUBLE PRECISION data type provides 8-byte storage for numbers using IEEE
     * floating-point notation.
     * </p>
     */
    DOUBLE(Types.DOUBLE),
    /**
     *
     * <p  align="justify" style="width:60%">
     * DECIMAL provides an exact numeric in which the precision and scale can be arbitrarily
     * sized. You can specify the precision (the total number of digits, both to the left and
     * the right of the decimal point) and the scale (the number of digits of the fractional
     * component). The amount of storage required is based on the precision.
     * </p>
     */
    DECIMAL(Types.DECIMAL),
    /**
     *
     * <p  align="justify" style="width:60%">
     * This is a character array (String) of variable length, though here we have fixed its
     * size to <code>32672</code>. In storage, VARCHAR(32672) is smart enough to store only the
     * length you need on a given row, unlike CHAR(LENGTH) which would always store as
     * may characters as its nominal length.
     * </p>
     */
    VARCHAR(Types.VARCHAR){

        /**
         *
         * Varchar datatype of a standard length equal to 32672.
         */
        @Override
        public String toString() {
            return "VARCHAR(32672)";
        }

        /**
         * Varchar datatype of user-defined length.
         */
        public String toString(int size){
            return "VARCHAR("+size+")";
        }
    },
    /**
     *
     * <p  align="justify" style="width:60%">
     * Datatype used to store very large texts like web page HTML codes or other kinds of
     * text. The LONG VARCHAR type allows storage of character strings with a maximum length
     * of 32,700 characters. It is identical to VARCHAR, except that you cannot specify a
     * maximum length when creating columns of this type.
     * </p>
     */
    LONG_VARCHAR(Types.LONGVARCHAR){

        @Override
        public String toString() {
            return "LONG VARCHAR";
        }
    },
    /**
     *
     * <p  align="justify" style="width:60%">
     * Here stands for a small word. <code>CHAR</code> has always a fixed size of
     * 1 character (as used in Decibell&copy;).
     * </p>
     */
    CHAR(Types.CHAR){

        @Override
        public String toString() {
            return "CHAR(1)";
        }
    },
    /**
     *
     * <p  align="justify" style="width:60%">
     * TIMESTAMP stores a combined DATE and TIME value to be stored. It permits a
     * fractional-seconds value of up to nine digits. The corresponding java datatype
     * is <code>java.sql.Timestamp</code>. Dates, times, and timestamps cannot be
     * mixed with one another in expressions. Example: <code>'1962-09-23 03:23:34.234'</code> .
     * </p>
     */
    TIMESTAMP(Types.TIMESTAMP),

    BIT(Types.BIT),

    VARBINARY(Types.VARBINARY){

        @Override
        public String toString() {
            return "VARCHAR (32672) FOR BIT DATA";
        }
        
        @Override
        public String toString(int size) {
            return "VARCHAR ("+size+")FOR BIT DATA";
        }
    },

    LONG_VARBINARY(Types.LONGVARBINARY){
        
        @Override
        public String toString() {
            return "LONG VARCHAR FOR BIT DATA";
        }
    },

    /**
     *
     * <p  align="justify" style="width:60%">
     * A Binary Large Object is used to store data in a table in binary format.
     * </p>
     */
    BLOB(Types.BLOB),

    /**
     *
     * <p  align="justify" style="width:60%">
     * This is not a valid SQL datatype, instead is a flag to denote that the type of
     * the undelying element should be retrieved from the corresponding java type declared
     * in the code. <code>VOID</code> can be any of the above types and the corresponding java
     * type, just for consistency, is set to <code>java.langg.Void</code>. This type, is not
     * intended to be used <em>as is</em> in any SQL operation, it is just a directive to
     * Decibell, to put effort to chose the correct type itself. <code>ANY</code> could
     * be an alternative name for <code>VOID</code>.
     * </p>
     */
    VOID(Types.BLOB);

    private int type;

    private SQLType(int type){
        this.type = type;
    }

    /**
     *
     * The class {@link Types Types} is widely used in lots of SQL-related applications to
     * identify the various SQL types by an integer number. This method returns this
     * integer for the SQLType object it is applied on. These integer constants are
     * JDBC specific but are used as a basis for the SQL types in Decibell in general.
     * @return
     *      SQL Type as an integer with respect to the java.sql.Types standards
     */
    public int getType(){
        return type;
    }

    /**
     *
     * String representation of a datatype with user-defined size.
     * @param size
     *      Adjustable size of the datatype (e.g. VARCHAR)
     * @return
     *      String representation datatype with the specified size.
     */
    public String toString(int size){return toString();};
}
