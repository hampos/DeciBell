/**
 *  Class : Infinity
 *  Date  : 20 Απρ 2010
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

package org.kinkydesign.decibell.db.util;

import org.kinkydesign.decibell.collections.Qualifier;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.derby.DerbyConnector;
import org.kinkydesign.decibell.db.derby.util.DerbyInfinity;
import org.kinkydesign.decibell.db.query.Proposition;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class Infinity {

    private enum Connection{
        DERBY,
        MYSQL,
        ORACLE
    }

    private Connection con;
    
    public Infinity(DbConnector con){
        if(con.getClass().equals(DerbyConnector.class)){
            this.con = Connection.DERBY;
        }else{

        }
    }

    public Object getInfinity(Proposition p){
        switch (p.getTableColumn().getColumnType()) {
            case INTEGER:
                if(p.getQualifier().equals(Qualifier.GREATER_EQUAL)
                        || p.getQualifier().equals(Qualifier.GREATER_THAN) ){
                    return getLeftInt();
                } else if(p.getQualifier().equals(Qualifier.LESS_EQUAL)
                        || p.getQualifier().equals(Qualifier.LESS_THAN)){
                    return getRightInt();
                } else {
                    return 0;
                }
            case REAL:
                if(p.getQualifier().equals(Qualifier.GREATER_EQUAL)
                        || p.getQualifier().equals(Qualifier.GREATER_THAN) ){
                    return getLeftFloat();
                } else if(p.getQualifier().equals(Qualifier.LESS_EQUAL)
                        || p.getQualifier().equals(Qualifier.LESS_THAN)){
                    return getRightFloat();
                } else {
                    return 0;
                }
            case DOUBLE:
                if(p.getQualifier().equals(Qualifier.GREATER_EQUAL)
                        || p.getQualifier().equals(Qualifier.GREATER_THAN) ){
                    return getLeftDouble();
                } else if(p.getQualifier().equals(Qualifier.LESS_EQUAL)
                        || p.getQualifier().equals(Qualifier.LESS_THAN)){
                    return getRightDouble();
                } else {
                    return 0;
                }
            case BIGINT:
                if(p.getQualifier().equals(Qualifier.GREATER_EQUAL)
                        || p.getQualifier().equals(Qualifier.GREATER_THAN) ){
                    return getLeftLong();
                } else if(p.getQualifier().equals(Qualifier.LESS_EQUAL)
                        || p.getQualifier().equals(Qualifier.LESS_THAN)){
                    return getRightLong();
                } else {
                    return 0;
                }
            default:
                return new String("%%");
        }
    }

    public int getLeftInt(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getLeftInt();
            default:
                return Integer.MIN_VALUE;
        }
    }

    public int getRightInt(){
        switch(con){
            case DERBY:
                return DerbyInfinity.getRightInt();
            default:
                return Integer.MAX_VALUE;
        }
    }

    public double getLeftFloat(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getLeftFloat();
            default:
                return Float.MIN_VALUE;
        }
    }

    public double getRightFloat(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getRightFloat();
            default:
                return Float.MAX_VALUE;
        }
    }

    public double getLeftDouble(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getLeftDouble();
            default:
                return Double.MIN_VALUE;
        }
    }

    public double getRightDouble(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getRightDouble();
            default:
                return Double.MAX_VALUE;
        }
    }

    public long getLeftLong(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getLeftLong();
            default:
                return Long.MIN_VALUE;
        }
    }

    public long getRightLong(){
        switch (con){
            case DERBY:
                return DerbyInfinity.getRightLong();
            default:
                return Long.MAX_VALUE;
        }
    }

}
