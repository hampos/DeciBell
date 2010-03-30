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
package org.kinkydesign.decibell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.kinkydesign.decibell.core.Component;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.TablesGenerator;
import org.reflections.Reflections;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeciBell implements JDeciBell {

    /**
     * A {@link DeciBell } object is a nice Wrapper to {@link DbConnector } offering
     * increased flexibility. This is the private DbConnector object held by Decibell.
     */
    private DbConnector connector = new DbConnector();

    private static Map<Class<? extends Component>, DeciBell> componentDBmap =
            new HashMap<Class<? extends Component>, DeciBell>();
    /**
     * The set of all user-specified components (Classes that extend {@link Component }).
     */
    Set<Class<? extends Component>> components = null;

    public void attach(Class<? extends Component> c) {
        c.asSubclass(Component.class);
        if (components == null) {
            components = new HashSet<Class<? extends Component>>();
        }
        this.components.add(c);
        componentDBmap.put(c, this);
    }

    public void start() {
        connector.connect();
        if (this.components == null) {
            Reflections reflections = new Reflections("");
            components = reflections.getSubTypesOf(Component.class);
        }
        TablesGenerator tables = new TablesGenerator(connector, components);
        tables.construct();

        StatementPool pool = new StatementPool(connector,10);
    }

    public void restart() {
        connector.disconnect();
        connector.killDerby();
        connector.connect();
    }

  
    public void reset() {
        connector.clearDB();
    }

    public void stop() {
        connector.disconnect();
        connector.killDerby();
    }

    public void setDriverHome(String driverHome) {
        connector.setDriverHome(driverHome);
    }

    public void setDriver(String driver) {
        connector.setDriver(driver);
    }

    public void setJavaOptions(String javaOptions) {
        connector.setJavaOptions(javaOptions);
    }

    public void setJavacmd(String javacmd) {
        connector.setJavacmd(javacmd);
    }

    public void setUser(String user) {
        connector.setUser(user);
    }

    public void setDbName(String dbName) {
        connector.setDbName(dbName);
    }
}
