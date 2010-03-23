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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TypeMap {

    private static Map<Class,SQLType> typeMap = new HashMap<Class,SQLType>();

    static{
        typeMap.put(int.class, SQLType.INTEGER);
        typeMap.put(Integer.class, SQLType.INTEGER);
        typeMap.put(Short.class, SQLType.SMALLINT);
        typeMap.put(short.class, SQLType.SMALLINT);
        typeMap.put(Long.class, SQLType.BIGINT);
        typeMap.put(long.class, SQLType.BIGINT);
        typeMap.put(Float.class, SQLType.REAL);
        typeMap.put(float.class, SQLType.REAL);
        typeMap.put(Double.class, SQLType.DOUBLE);
        typeMap.put(double.class, SQLType.DOUBLE);
        typeMap.put(Boolean.class, SQLType.BIT);
        typeMap.put(boolean.class, SQLType.BIT);
        typeMap.put(BigDecimal.class, SQLType.DECIMAL);
        typeMap.put(String.class, SQLType.VARCHAR);
        typeMap.put(char[].class, SQLType.VARCHAR);
        typeMap.put(char.class, SQLType.CHAR);       
        typeMap.put(Timestamp.class , SQLType.TIMESTAMP);
        typeMap.put(Void.class, SQLType.VOID);
    }

    public static SQLType getSQLType(Class c){
        return (typeMap.get(c)!=null)?typeMap.get(c):SQLType.VARBINARY;
    }

    public static Set<Class> getJavaTypes(SQLType type){
        Set<Class> javaTypes =  new HashSet<Class>();
        for(Class c : typeMap.keySet()){
            if(typeMap.get(c).equals(type)){
                javaTypes.add(c);
            }
        }
        if(javaTypes.isEmpty()){
            javaTypes.add(Object.class);
        }
        return javaTypes;
    }

    public static boolean isSubClass(Class c, Class sup){
        try{
            c.asSubclass(sup);
        }catch(ClassCastException ex){
            return false;
        }
        return true;
    }

}
