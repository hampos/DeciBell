/**
 *  Class : DeciBellReflectUtils
 *  Date  : May 9, 2010
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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeciBellReflectUtils {

    /**
     * <p  align="justify" style="width:60%">
     * Returns all fields of a given class irrespective of their access level (private,
     * protected, public or package-range). The fields of all superclasses are also included
     * in the search. The elements in the array returned are not sorted and are not in
     * any particular order. If the class does not contain any fields, this method
     * returns a set of length 0 (empty set but not <code>null</code>.
     * </p>
     * @param c
     * @param setAccessible
     *      If set to <code>true</code> renders all returned fields accessible
     *      using the method {@link Field#setAccessible(boolean) Field.setAccessible(boolean)}.
     *      while if set to false the fields retain their original access level.
     * @return
     *      Set of Fields (Set&lt;Field&gt;) with all fields in the given class and its superclasses.
     * @throws SecurityException
     *      If the attempt to set a field accessible fails or some security manager
     *      prohibits the access to the class or the package.
     */
    public static Set<Field> getAllFields(final Class c, final boolean setAccessible) throws SecurityException {
        Set<Field> fields = new HashSet<Field>();
        for (Field f : c.getFields()){
            fields.add(f);
            if (setAccessible) f.setAccessible(true);
        }
        for (Field f : c.getDeclaredFields()){
            fields.add(f);
            if (setAccessible) f.setAccessible(true);
        }
        return fields;
    }

}