/**
 *  Class : Infinity
 *  Date  : 15 Απρ 2010
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

/**
 * <p  align="justify" style="width:60%">
 * A collection of extreme value for numeric datatypes supported in Derby. It is
 * important to note that the bounds provided here for the Derby datatypes are
 * significantly different from the bounds of their corresponging Java datatypes. For
 * example the maximum integer value in java is 2<sup>31</sup>-1 while in SQL it is
 * -2147483648 (which is higher).
 * </p>
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public final class DerbyInfinity {

    private static final int
            LEFT_INT_INF = -2147483648,
            RIGHT_INT_INF = 2147483647;
    private static final double
            LEFT_REAL_INF = -3.402E+38,
            RIGHT_REAL_INF = 3.402E+38;
    private static final double
            LEFT_DOUBLE_INF =  -1.79769E+308,
            RIGHT_DOUBLE_INF =  1.79769E+308;
    private static final long
            LEFT_LONG_INF = -9223372036854775808L,
            RIGHT_LONG_INF = 9223372036854775807L;
    private static final short
            LEFT_SMALLINT_INF = -32768,
            RIGHT_SMALLINT_INF = 32767;

    /**
     * <p  align="justify" style="width:60%">
     * The minimum (negative) integer value accepted by the <code>INTEGER</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Minimum Integer
     */
    public static int getLeftInt(){
        return LEFT_INT_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The maximum (positive) integer value accepted by the <code>INTEGER</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Maximum Integer
     */
    public static int getRightInt(){
        return RIGHT_INT_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The minimum (negative) float value accepted by the <code>REAL</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Minimum Float
     */
    public static float getLeftFloat(){
        return (float) LEFT_REAL_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The maximum (positive) float value accepted by the <code>REAL</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Maximum Float
     */
    public static float getRightFloat(){
        return (float) RIGHT_REAL_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The maximum (positive) double value accepted by the <code>DOUBLE PRECISION</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Maximum Double Precision Value
     */
    public static double getRightDouble(){
        return RIGHT_DOUBLE_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The minimum (negative) double value accepted by the <code>DOUBLE PRECISION</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Minimum Double Precision Value
     */
    public static double getLeftDouble(){
        return LEFT_DOUBLE_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The minimum (negative) short value accepted by the <code>SMALL INT</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Minimum Short Value
     */
    public static short getLeftShort(){
        return LEFT_SMALLINT_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The minimum (negative) short value accepted by the <code>SMALL INT</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Minimum Short Value
     */
    public static short getRightShort(){
        return RIGHT_SMALLINT_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The minimum (negative) long value accepted by the <code>BIGINT</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Minimum Long Value
     */
    public static long getLeftLong(){
        return LEFT_LONG_INF;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The maximum (positive) long value accepted by the <code>BIGINT</code>
     * datatype of Derby.
     * </p>
     * @return
     *      Maximum Long Value
     */
    public static long getRightLong(){
        return RIGHT_LONG_INF;
    }
}
