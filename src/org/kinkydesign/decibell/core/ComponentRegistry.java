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
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;


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

    private static Map<DbConnector, ComponentRegistry> registries = new HashMap<DbConnector,ComponentRegistry>();

    private Map<Class<? extends Component>,JTable> components = new LinkedHashMap<Class<? extends Component>,JTable>();
    private Set<JRelationalTable> relations = new HashSet<JRelationalTable>();

    public ComponentRegistry(DbConnector con) {
        if(!registries.containsKey(con)){
            registries.put(con, this);
        }
    }

    public static ComponentRegistry getRegistry(DbConnector con){
        return registries.get(con);
    }

    public Collection<JTable> values() {
        return components.values();
    }

    public int size() {
        return components.size();
    }

    public JTable remove(Object key) {
        return components.remove((Class<? extends Component>)key);
    }

    public void putAll(Map<? extends Class<? extends Component>, ? extends Table> m) {
        components.putAll(m);
    }

    public JTable put(Class<? extends Component> key, Table value) {
        return components.put(key, value);
    }

    public Set<Class<? extends Component>> keySet() {
        return components.keySet();
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public JTable get(Object key) {
        return components.get((Class<? extends Component>)key);
    }

    public Set<Entry<Class<? extends Component>, JTable>> entrySet() {
        return components.entrySet();
    }

    public boolean containsValue(Object value) {
        return components.containsValue((Table)value);
    }

    public boolean containsKey(Object key) {
        return components.containsKey((Class<? extends Component>)key);
    }

    public void clear() {
        components.clear();
    }

    public void setRelationTable(JRelationalTable relTable) {
        relations.add(relTable);
    }

    public Set<JRelationalTable> getRelationTables() {
        return relations;
    }

    public Set<Class<? extends Component>> getParents(DbConnector con, Class<? extends Component> child) {
        Set<Class<? extends Component>> parents = new HashSet<Class<? extends Component>>();
        for (TableColumn col : get(child).getForeignKeyColumns()) {
            parents.add(col.getReferencesClass());
        }
        return parents;
    }

    public Set<Class<? extends Component>> getChildren(DbConnector con, Class<? extends Component> parent) {
        Set<Class<? extends Component>> children = new HashSet<Class<? extends Component>>();
        for (Class c : components.keySet()) {
            for (TableColumn col : components.get(c).getForeignKeyColumns()) {
                if (col.getReferencesClass().equals(parent)) {
                    children.add(col.getReferencesClass());
                }
            }
        }
        return children;
    }
}
