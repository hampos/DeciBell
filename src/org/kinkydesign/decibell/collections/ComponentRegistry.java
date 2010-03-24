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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.kinkydesign.decibell.core.Component;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ComponentRegistry implements Map<DbConnector, Map<Class<? extends Component>, Table>> {

    private static Map<DbConnector, ComponentRegistry> registries = new HashMap<DbConnector,ComponentRegistry>();

    private static Map<DbConnector, Map<Class<? extends Component>, Table>> componentRegistry =
            new HashMap<DbConnector, Map<Class<? extends Component>, Table>>();
    private static Map<DbConnector, Set<Table>> reltables = new HashMap<DbConnector, Set<Table>>();

    private Map<Class<? extends Component>,Table> components = null;

    public ComponentRegistry(DbConnector con) {
        if (!containsKey(con)) {
            put(con, new LinkedHashMap<Class<? extends Component>, Table>());
        }
        if (!reltables.containsKey(con)) {
            reltables.put(con, new HashSet<Table>());
        }
    }

    public static ComponentRegistry getRegistry(DbConnector con){
        return registries.get(con);
    }

    public Collection<Map<Class<? extends Component>, Table>> values() {
        return componentRegistry.values();
    }

    public int size() {
        return componentRegistry.size();
    }

    public Map<Class<? extends Component>, Table> remove(Object key) {
        return componentRegistry.remove((DbConnector) key);
    }

    public void putAll(Map<? extends DbConnector, ? extends Map<Class<? extends Component>, Table>> m) {
        componentRegistry.putAll(m);
    }

    public Map<Class<? extends Component>, Table> put(DbConnector key, Map<Class<? extends Component>, Table> value) {
        return componentRegistry.put(key, value);
    }

    public Set<DbConnector> keySet() {
        return componentRegistry.keySet();
    }

    public boolean isEmpty() {
        return componentRegistry.isEmpty();
    }

    public Map<Class<? extends Component>, Table> get(Object key) {
        return componentRegistry.get((DbConnector) key);
    }

    public Set<Entry<DbConnector, Map<Class<? extends Component>, Table>>> entrySet() {
        return componentRegistry.entrySet();
    }

    public boolean containsValue(Object value) {
        return componentRegistry.containsValue((Map<Class<? extends Component>, Table>) value);
    }

    public boolean containsKey(Object key) {
        return componentRegistry.containsKey((DbConnector) key);
    }

    public void clear() {
        componentRegistry.clear();
    }

    public int size(DbConnector con) {
        return componentRegistry.get(con).size();
    }

    public boolean isEmpty(DbConnector con) {
        return componentRegistry.get(con).isEmpty();
    }

    public boolean containsKey(DbConnector con, Object key) {
        return componentRegistry.get(con).containsKey((Class<? extends Component>) key);
    }

    public boolean containsValue(DbConnector con, Object value) {
        return componentRegistry.get(con).containsValue((Table) value);
    }

    public Object remove(DbConnector con, Object key) {
        return componentRegistry.get(con).remove((Class<? extends Component>) key);
    }

    public Table get(DbConnector con, Object key) {
        return componentRegistry.get(con).get((Class<? extends Component>) key);
    }

    public Object put(DbConnector con, Object key, Object value) {
        return componentRegistry.get(con).put((Class<? extends Component>) key, (Table) value);
    }

    public void setRelationTable(DbConnector con, Table relTable) {
        reltables.get(con).add(relTable);
    }

    public Set<Table> getRelationTables(DbConnector con) {
        return reltables.get(con);
    }

    public Set<Class<? extends Component>> getParents(DbConnector con, Class<? extends Component> child) {
        Set<Class<? extends Component>> parents = new HashSet<Class<? extends Component>>();
        for (TableColumn col : get(con, child).getForeignKeyColumns()) {
            parents.add(col.getReferencesClass());
        }
        return parents;
    }

    public Set<Class<? extends Component>> getChildren(DbConnector con, Class<? extends Component> parent) {
        Set<Class<? extends Component>> children = new HashSet<Class<? extends Component>>();
        Map<Class<? extends Component>, Table> components =
                (Map<Class<? extends Component>, Table>) get(con);
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
