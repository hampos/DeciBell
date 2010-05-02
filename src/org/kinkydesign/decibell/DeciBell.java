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
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.TablesGenerator;
import org.kinkydesign.decibell.db.derby.DerbyConnector;
import org.kinkydesign.decibell.db.derby.DerbyTablesGenerator;
import org.reflections.Reflections;

/**
 * <p  align="justify" style="width:60%">
 * Class used to manipulate a database connection, define object to DB entities mappings
 * and handle the database server (start/stop/restart).
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeciBell {

    /**
     * A {@link DeciBell } object is a nice Wrapper to {@link DbConnector } offering
     * increased flexibility. This is the private DbConnector object held by Decibell.
     */
    private DbConnector connector = new DerbyConnector();
    private static Map<Class<? extends Component>, DeciBell> componentDBmap =
            new HashMap<Class<? extends Component>, DeciBell>();
    /**
     * The set of all user-specified components (Classes that extend {@link Component }).
     */
    Set<Class<? extends Component>> components = null;

    /**
     * <p  align="justify" style="width:60%">
     * Attach a new java class to the relational database structure. One or more
     * tables are going to be created due to this attachment so that the submitted
     * class will have a counterpart in the database.
     * </p>
     * @param c
     *      The class to be attached to the collection of relational entities.
     */
    public void attach(Class<? extends Component> c) {
        c.asSubclass(Component.class);
        if (components == null) {
            components = new HashSet<Class<? extends Component>>();
        }
        this.components.add(c);
        componentDBmap.put(c, this);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Start the connection to the database and, if the database does not exist,
     * create it using the entity-relation structure perscribed by the attached classes
     * and the annotations therein. Upon startup, some SQL statements are prepared.
     * </p>
     */
    public void start() {
        connector.connect();
        if (this.components == null) {
            Reflections reflections = new Reflections("");
            components = reflections.getSubTypesOf(Component.class);
        }
        TablesGenerator tables = new DerbyTablesGenerator(connector, components);
        tables.construct();
        StatementPool pool = new StatementPool(connector, 10);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Restarts the database connection. Disconnects from the database, shuts down the
     * Derby server and starts the connection again. Should be performed if there is
     * suspicion that Decibell or Database do not respond.
     * </p>
     */
    public void restart() {
        connector.disconnect();
        connector.killServer();
        connector.connect();
    }

    /**
     * <p  align="justify" style="width:60%">
     * Clears the whole database structure (removes all tables from the database)
     * and removes the user as well. Be careful when invoking this method because
     * <b>all data in this database will be permanently lost!</b>. The database
     * structure will be also lost. Use methods {@link DeciBell#attach(java.lang.Class) attach}
     * and {@link DeciBell#start() start} to reconstruct a database.
     * </p>
     */
    public void reset() {
        connector.clearDB();
    }

    /**
     * <p  align="justify" style="width:60%">
     * Stops the connection and kills the Database server.
     * </p>
     */
    public void stop() {
        connector.disconnect();
        connector.killServer();
        try {
            while (connector.isConnected() || connector.isServerRunning()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("Interrupted while stopping...", ex);
        }
    }

    /**
     * <p  align="justify" style="width:60%">
     * Define an path for the installation folder of the Driver
     * server.
     * </p>
     * @param driverHome
     *      Derby Home.
     */
    public void setDriverHome(String driverHome) {
        connector.setDriverHome(driverHome);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Choose a driver for the connection to the database server.
     * The default driver is <code>org.apache.derby.jdbc.EmbeddedDriver</code>. Use
     * the full name of the driver and be sure that you have included the corresponding
     * class in your classpath.
     * </p>
     * @param driver
     *      Full name of derby driver.
     */
    public void setDriver(String driver) {
        connector.setDatabaseDriver(driver);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Java options used when the database server is started.
     * </p>
     * @param javaOptions
     *      Java options for the invokation of the derby server startup command.
     */
    public void setJavaOptions(String javaOptions) {
        connector.setJavaOptions(javaOptions);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Java command or path to it. By default is set to <code>java</code> but in some
     * cases (e.g. on Debian machines) but need to modify it to <code>/usr/bin/java</code>
     * or some other path or command.
     * </p>
     * @param javacmd
     *      java command
     */
    public void setJavacmd(String javacmd) {
        connector.setJavacmd(javacmd);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Define the user which connects to the database. Note that in <code>derby</code>
     * (The database server used by DeciBell) every user corresponds to a database
     * SCHEMA.
     * </p>
     * @param user
     *      Database user
     */
    public void setUser(String user) {
        connector.setUser(user);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Choose a name for the database you want to create or a name for an existing
     * database. Then the URL of the database connection will be
     * <code>jdbc:derby://{hostname}:{port}/{database_name}</code>. Set the database
     * connection port and hostname usign the corresponding methods. Database names have
     * a URI-like structure; for example <code>database/decibell/db1</code> is an
     * acceptable name for your database.
     * </p>
     * @param dbName
     *      The name of the database.
     */
    public void setDbName(String dbName) {
        connector.setDbName(dbName);
    }

    /**
     * <p  align="justify" style="width:60%">
     * protected method that returns this DeciBell's DbConnector object. To be used
     * only by Component.class
     * </p>
     * @return this DeciBell's DbConnector.
     */
    public DbConnector getDbConnector() {
        return connector;
    }

    public boolean isServerRunning() {
        return connector.isServerRunning();
    }

    public boolean isConnected() {
        return connector.isConnected();
    }

    public String getUser() {
        return connector.getUser();
    }

    public String getHost() {
        return connector.getHost();
    }

    public String getDriverHome() {
        return connector.getDriverHome();
    }

    public String getDbName() {
        return connector.getDbName();
    }

    public String getDatabaseUrl() {
        return connector.getDatabaseUrl();
    }

    public String getDatabaseDriver() {
        return connector.getDatabaseDriver();
    }
}
