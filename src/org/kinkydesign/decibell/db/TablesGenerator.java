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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;

/**
 * This abstract class provides an interface for the table creation process.
 * Each database driver implementation has to implement this class and use
 * it's own specific process to perform the database construction.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class TablesGenerator {

    /**
     * The registry on which the created tables will be inserted.
     */
    protected ComponentRegistry registry = null;

    /**
     * The DbConnector that defines the database on which the table creation
     * will be executed.
     */
    protected DeciBell db = null;

    /**
     * The Component classes that must be included in the database creation.
     */
    protected Set<Class<? extends Component>> components = null;

    /**
     * Relational fields, meaning fields that must be implemented by
     * many-to-many relations such as ArrayLists of foreign objects.
     */
    protected Set<Field> relations = new HashSet<Field>();

    /**
     * Constructs a new TablesGenerator for a specific database and a set
     * of components.
     * @param connector a DbConnector that identifies the database on which the
     * creation process will take place.
     * @param components a Set of Components that must be included in the database.
     */
    public TablesGenerator(DeciBell db, Set<Class<? extends Component>> components){
        this.db = db;
        this.components = components;
        registry = new ComponentRegistry(db.getDbConnector());
    }

    /**
     * Constructs all tables in the database and inserts them to the registry.
     */
    public abstract void construct();

    /**
     * Returns true if a given class is a sub class of Component.class
     * @param c a Class that needs to learn if it inherits Component.class
     * @return True if the given class is a sub class of Component.class
     */
    protected boolean isComponent(Class c) {
        return TypeMap.isSubClass(c, Component.class);
    }

    /**
     * Returns true if a given class is a sub class of Collection.class
     * @param c a Class that needs to learn if it inherits Collection.class
     * @return True if the given class is a sub class of Collection.class
     */
    protected boolean isCollection(Class c) {
        return TypeMap.isSubClass(c, Collection.class);
    }

    /**
     * Returns true if a given class is a sub class of List.class
     * @param c a Class that needs to learn if it inherits List.class
     * @return True if the given class is a sub class of List.class
     */
    protected boolean isList(Class c){
        return TypeMap.isSubClass(c, List.class);
    }
    

    /**
     * Returns true if a given class is a sub class of Set.class
     * @param c a Class that needs to learn if it inherits Set.class
     * @return True if the given class is a sub class of Set.class
     */
    protected boolean isSet(Class c){
        return TypeMap.isSubClass(c, Set.class);
    }

}
