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
 *
 * A collection of extreme value for numeric datatypes supported in Derby.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DerbyInfinity {

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

    public static int getLeftInt(){
        return LEFT_INT_INF;
    }

    public static int getRightInt(){
        return RIGHT_INT_INF;
    }

    public static float getLeftFloat(){
        return (float) LEFT_REAL_INF;
    }

    public static float getRightFloat(){
        return (float) RIGHT_REAL_INF;
    }

    public static double getRightDouble(){
        return RIGHT_DOUBLE_INF;
    }

    public static double getLeftDouble(){
        return LEFT_DOUBLE_INF;
    }

    public static short getLeftShort(){
        return LEFT_SMALLINT_INF;
    }

    public static short getRightShort(){
        return RIGHT_SMALLINT_INF;
    }

    public static long getLeftLong(){
        return LEFT_LONG_INF;
    }

    public static long getRightLong(){
        return RIGHT_LONG_INF;
    }
}
