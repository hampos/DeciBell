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

import org.kinkydesign.decibell.core.Component;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JDeciBell {

    /**
     * Attach a new java class to the relational database structure. One or more
     * tables are goind to be created due to this attachment so that the submitted
     * class will have a counterpart in the database.
     * @param c
     *      The class to be attached to the collection of relational entities.
     */
    public void attach(Class<? extends Component> c);

    /**
     * Start the connection to the database and, if the database does not exist,
     * create it using the entity-relation structure perscribed by the attached classes
     * and the annotations therein. Upon startup, some SQL statements are prepared.
     */
    public void start();

    /**
     * Restarts the database connection. Disconnects from the database, shuts down the
     * Derby server and starts the connection again. Should be performed if there is
     * suspision that Decibell or Derby do not respond.
     */
    public void restart();

    /**
     * Clears the whole database structure (removes all tables from the database)
     * and removes the user as well. Be careful when invoking this method because
     * <b>all data in this database will be permanently lost!</b>.
     */
    public void reset();

    /**
     * Stops the connection and kills the Derby server.
     */
    public void stop();

    /**
     * Define an alternative path for the installation folder of the Derby
     * server. In the Derby manual this parameter is refered to as <code>$DERBY_HOME</code>.
     * @param driverHome
     *      Derby Home.
     */
    public void setDriverHome(String driverHome);

    /**
     * Choose an alternative driver for the connection to the Derby database server.
     * The default driver is <code>org.apache.derby.jdbc.EmbeddedDriver</code>. Use
     * the full name of the driver and be sure that you have included the corresponding
     * class in your classpath.
     * @param driver
     *      Full name of derby driver.
     */
    public void setDriver(String driver);

    /**
     * Java options used when the derby server is started.
     * @param javaOptions
     *      Java options for the invokation of the derby server startup command.
     */
    public void setJavaOptions(String javaOptions);

    /**
     * Java command or path to it. By default is set to <code>java</code> but in some
     * cases (e.g. on Debian machines) but need to modify it to <code>/usr/bin/java</code>
     * or some other path or command.
     * @param javacmd
     *      java command
     */
    public void setJavacmd(String javacmd);

    /**
     * Define the user which connects to the database. Note that in <code>derby</code>
     * (The database server used by DeciBell) every user corresponds to a database
     * SCHEMA.
     * @param user
     *      Database user
     */
    public void setUser(String user);

    /**
     * Choose a name for the database you want to create or a name for an existing
     * database. Then the URL of the database connection will be
     * <code>jdbc:derby://{hostname}:{port}/{database_name}</code>. Set the database
     * connection port and hostname usign the corresponding methods. Database names have
     * a URI-like structure; for example <code>database/decibell/db1</code> is an
     * acceptable name for your database.
     * @param dbName
     *      The name of the database.
     */
    public void setDbName(String dbName);

}
