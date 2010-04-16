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
package org.kinkydesign.decibell.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.kinkydesign.decibell.annotations.Constraint;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.exceptions.NoPrimaryKeyException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class TablesGenerator {

    protected ComponentRegistry registry = null;
    protected DbConnector connector = null;
    protected Set<Class<? extends Component>> components = null;
    protected Set<Field> relations = new HashSet<Field>();

    public TablesGenerator(DbConnector connector, Set<Class<? extends Component>> components){
        this.connector = connector;
        this.components = components;
        registry = new ComponentRegistry(connector);
    }

    public abstract void construct();

    protected boolean isComponent(Class c) {
        return TypeMap.isSubClass(c, Component.class);
    }

    protected boolean isCollection(Class c) {
        return TypeMap.isSubClass(c, Collection.class);
    }

    protected boolean isList(Class c){
        return TypeMap.isSubClass(c, List.class);
    }

    protected boolean isSet(Class c){
        return TypeMap.isSubClass(c, Set.class);
    }

}
