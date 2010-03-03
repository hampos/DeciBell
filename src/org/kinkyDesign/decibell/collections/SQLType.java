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
package org.kinkyDesign.decibell.collections;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum SQLType implements JSQLType {

    /**
     * Integer in SQL is the analogue of Integer in java. Lower and Higher Bounds are
     * -2147483648 and 2147483647 respectively.<code>INTEGER</code> provides 4 bytes
     * of storage for integer values.
     */
    INTEGER(Integer.class),
    /**
     * <code>SMALLINT</code> is the counterpart of <code>short </code>. Higher and Lower
     * limits are 32767 and -32768 respectively. SMALLINT provides 2 bytes of storage.
     */
    SMALLINT(Short.class),
    /**
     * <code>BIGINT</code> is like <code>Long</code> in java. It accepts values in the
     * range from -9223372036854775808 up to 9223372036854775807.
     */
    BIGINT(Long.class),
    REAL(Float.class),
    /**
     * The DOUBLE PRECISION data type provides 8-byte storage for numbers using IEEE
     * floating-point notation.
     */
    DOUBLE(Double.class),
    /**
     * DECIMAL provides an exact numeric in which the precision and scale can be arbitrarily
     * sized. You can specify the precision (the total number of digits, both to the left and
     * the right of the decimal point) and the scale (the number of digits of the fractional
     * component). The amount of storage required is based on the precision.
     */
    DECIMAL(BigDecimal.class),    
    /**
     * This is a character array (String) of variable length, though here we have fixed its
     * size to <code>255</code>. In storage, VARCHAR(255) is smart enough to store only the
     * length you need on a given row, unlike CHAR(255) which would always store 255 characters.
     */
    VARCHAR(String.class){

        @Override
        public String toString() {
            return "VARCHAR(255)";
        }
    },
    /**
     * Datatype used to store very large texts like web page HTML codes or other kinds of
     * text. The LONG VARCHAR type allows storage of character strings with a maximum length
     * of 32,700 characters. It is identical to VARCHAR, except that you cannot specify a
     * maximum length when creating columns of this type.
     */
    LONG_VARCHAR(String.class) {

        @Override
        public String toString() {
            return "LONG VARCHAR";
        }
    },
    /**
     * Here stands for a small word. <code>CHAR</code> has always a fixed size of
     * 10 characters (as used in Decibell&copy;).
     */
    CHAR(String.class){
        @Override
        public String toString(){
            return "CHAR(10)";
        }
    },
    TIMESTAMP(Timestamp.class);

    private Class javaDataType = String.class;

    private SQLType(){
    }


    private SQLType(Class javaDataType){
        this();
        this.javaDataType = javaDataType;
    }

    public Class getJavaType(){
       return javaDataType;
    }

    public String getSqlType(){
        return toString();
    }


    public SQLType fromJavaType(Class javaType){
        for (SQLType type : values()){
            if (type.getJavaType() == javaType) return type;
        }
        return this.VARCHAR;
    }


    public boolean isCastAs(SQLType other){
        throw new UnsupportedOperationException();
    }

}
