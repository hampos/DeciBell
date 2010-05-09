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
package org.kinkydesign.decibell.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;


/**
 *
 * A special mapping between components and tables, where to every {@link Component component class}
 * attached to the {@link DeciBell } there is a corresponding table which is turn is found
 * in the database once it is successfully created. This stands for an easy correspondance
 * between components and tables.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ComponentRegistry {

    /**
     * a static Map that maps DbConnector objects to specific
     * ComponentRegistry objects that contain all class and table info
     * about a specific database.
     */
    private static Map<DbConnector, ComponentRegistry> registries = new HashMap<DbConnector,ComponentRegistry>();

    /**
     * Maps Component Classes to their according database tables.
     * Declared as LinkedHashMap because the table ierarchy must be mantained
     * in the table creation process.
     */
    private Map<Class<? extends Component>,JTable> components = new LinkedHashMap<Class<? extends Component>,JTable>();

    /**
     * A Set that holds all relational tables for a specific database.
     * As Relational we consider tables that are automatically generated
     * by DeciBell and server the purpose of creating a many-to-many relation
     * between two other tables.
     */
    private Set<JRelationalTable> relations = new HashSet<JRelationalTable>();

    /**
     * Constructor that inserts a new DbConnector to the registry.
     * @param con a DbConnector object that represents a specific database.
     */
    public ComponentRegistry(DbConnector con) {
        if(!registries.containsKey(con)){
            registries.put(con, this);
        }
    }

    /**
     * Gets the ComponentRegistry coresponding to a specific database.
     * @param con a DbConnector object that represents a specific database.
     * @return a ComponentRegistry coresponding a given DbConnector.
     */
    public static ComponentRegistry getRegistry(DbConnector con){
        return registries.get(con);
    }

    /**
     * Gets all tables from a given database's registry. This does not include
     * relational tables.
     * @return a Collection of all JTable objects in the registry. No relational
     * tables.
     */
    public Collection<JTable> values() {
        return components.values();
    }

    /**
     * The size of the Map that holds all database tables. This does not include
     * relational tables.
     * @return int size of the database registry.
     */
    public int size() {
        return components.size();
    }

    /**
     * Removes a Component Class from the registry. Also removes the database
     * table coresponding to that Component.
     * @param key a Componenent.class object.
     * @return the JTable that coresponds to the removed Component.
     */
    public JTable remove(Object key) {
        return components.remove((Class<? extends Component>)key);
    }

    /**
     * Adds the given map to the existing one.
     * @param m a Map that maps Component classes to database Tables.
     */
    public void putAll(Map<? extends Class<? extends Component>, ? extends JTable> m) {
        components.putAll(m);
    }

    /**
     * Adds a new Component - JTable pair to the registry.
     * @param key a Component.class object.
     * @param value a database table.
     * @return the previous JTable associated with the specific Component.
     */
    public JTable put(Class<? extends Component> key, JTable value) {        
        return components.put(key, value);
    }

    /**
     * Returns a Set containing all Components in the registry.
     * @return all Component.class objects in the registry.
     */
    public Set<Class<? extends Component>> keySet() {
        return components.keySet();
    }

    /**
     * Returns true if the registry has no components and tables.
     * @return True if the registry is empty.
     */
    public boolean isEmpty() {
        return components.isEmpty();
    }

    /**
     * Returns the JTable value associated with a given Component.
     * @param key a Component.class object
     * @return The table associated with the given Component.
     */
    public JTable get(Object key) {
        return components.get((Class<? extends Component>)key);
    }

    /**
     * Returns a Set view of the Component-JTable mappings contained in the
     * database. Changes to the Set are bound to change the original Map.
     * @return a Set of
     */
    public Set<Entry<Class<? extends Component>, JTable>> entrySet() {
        return components.entrySet();
    }

    /**
     * Returns true if the registry contains the specified JTable.
     * @param value a JTable object.
     * @return True if the JTable exists in the registry.
     */
    public boolean containsValue(Object value) {
        return components.containsValue((JTable)value);
    }

    /**
     * Returns true if the registry contains the specified Component.
     * @param key a Component.class object
     * @return True if the Component exists in the registry.
     */
    public boolean containsKey(Object key) {
        return components.containsKey((Class<? extends Component>)key);
    }

    /**
     * Clears the registry from Components and JTables. This does not include
     * relational tables.
     */
    public void clear() {
        components.clear();
    }

    /**
     * Adds a new relational table to the registry.
     * @param relTable a JRelationalTable to be added to the registry.
     */
    public void setRelationTable(JRelationalTable relTable) {
        relations.add(relTable);
    }

    /**
     * Returns a Set containing all relational tables from the registry.
     * @return a Set of all JRelationalTable objects in the registry.
     */
    public Set<JRelationalTable> getRelationTables() {
        return relations;
    }

    /**
     * Returns all parent components of a specified component in the database
     * hierarchy.
     * @param child a Component class that needs it's parents found.
     * @return A Set of Component classes that parent the specified Component.
     */
    public Set<Class<? extends Component>> getParents(Class<? extends Component> child) {
        Set<Class<? extends Component>> parents = new HashSet<Class<? extends Component>>();
        for (JTableColumn col : get(child).getForeignKeyColumns()) {
            parents.add(col.getReferencesClass());
        }
        return parents;
    }

    /**
     * Returns all child components of a specified component in the database
     * hierarchy.
     * @param parent a Component class that needs it's children found.
     * @return A Set of Component classes that have the specified Component
     * as parent.
     */
    public Set<Class<? extends Component>> getChildren(Class<? extends Component> parent) {
        Set<Class<? extends Component>> children = new HashSet<Class<? extends Component>>();
        for (Class c : components.keySet()) {
            for (JTableColumn col : components.get(c).getForeignKeyColumns()) {
                if (col.getReferencesClass().equals(parent)) {
                    children.add(col.getReferencesClass());
                }
            }
        }
        return children;
    }
}
