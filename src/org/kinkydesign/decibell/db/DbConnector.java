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

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A Connector to a database. Each connector represents a specific database
 * and is used as a token to be associated with it.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DbConnector {

    private String user;
    private String password;
    private String urlBase;
    private String dbName;
    private String javacmd;
    private String javaOptions;
    private String driverHome;
    private String host;
    private int port;
    private String databaseDriver;
    private Connection connection;
    private final Runtime runtime = Runtime.getRuntime();

    /**
     * Constructs a new DbConnector object.
     */
    public DbConnector() {
    }

    /**
     * Returns the name of the database driver.
     * @return String - name of the database driver.
     */
    public String getDatabaseDriver() {
        return databaseDriver;
    }

    /**
     * Returns the name of the database.
     * @return String - database name.
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Returns the location of the driver in the filesystem.
     * @return String - the driver location.
     */
    public String getDriverHome() {
        return driverHome;
    }

    /**
     * Returns the name of the database host.
     * @return String - hostname.
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the driver's java options.
     * @return String - java options.
     */
    public String getJavaOptions() {
        return javaOptions;
    }

    /**
     * Returns the system's java command.
     * @return String -  java command.
     */
    public String getJavacmd() {
        return javacmd;
    }

    /**
     * Returns the password used to access the database.
     * @return String - password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the port on which the database listens.
     * @return int - database port
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the application's java runtime.
     * @return the Runtime object associated with the current application.
     */
    public Runtime getRuntime() {
        return runtime;
    }

    /**
     * Returns the connector's url base, meaning the first part of the
     * database url, preceding the database name.
     * @return the connector's url base String.
     */
    public String getUrlBase() {
        return urlBase;
    }

    /**
     * Returns the current username in the connector.
     * @return String - username
     */
    public String getUser() {
        return user;
    }

    /**
     * Get the complete URL of the database connection as a String.
     * @return
     *      URL of database connection.
     */
    public String getDatabaseUrl() {
        return urlBase + host + ":" + port + "/" + dbName + ";user=" + user;
    }

    /**
     * Sets an SQL Connection to the connector.
     * @param connection a java.sql.Connection object for the connector
     * to connecto to.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sets the database driver name.
     * @param databaseDriver a database name String.
     */
    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    /**
     * Sets the database password.
     * @param password a database password String.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the port on which the database listens.
     * @param port a database port int.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Sets the url base on which the database url is constructed.
     * @param urlBase a base url String.
     */
    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    /**
     * Sets a name for the database you want to create or a name for an existing
     * database.
     * @param dbName
     *      The name of the database.
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Sets the full path to the Driver installation folder.
     * @param driverHome
     *      Complete path to driver installation folder.
     */
    public void setDriverHome(String driverHome) {
        this.driverHome = driverHome;
    }

    /**
     * Sets the name of the database host.
     * @param host a database host String.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Sets the java options that will customize the connector.
     * @param javaOptions a java options String.
     */
    public void setJavaOptions(String javaOptions) {
        this.javaOptions = javaOptions;
    }

    /**
     * Sets the system's java command keyword.
     * @param javacmd a java command String.
     */
    public void setJavacmd(String javacmd) {
        this.javacmd = javacmd;
    }

    /**
     * Sets the database connection username.
     * @param user a username String.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Starts a new connection with the database. No action is taken
     * if the driver is already connected to the database.
     */
    public abstract void connect();

    /**
     * Kills the process running the database driver.
     */
    public abstract void killServer();

    /**
     * Returns true if the database server is already running.
     * @return True if the database server is already running.
     */
    public abstract boolean isServerRunning();

    /**
     * Clears the database from all entries and tables. Use with caution - loss
     * of data is highly probable.
     */
    public abstract void clearDB();

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Returns the java.sql.Connection object contained in the DbConnector.
     * @return the connector's java.sql.Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Returns true if the driver is already connected to the database.
     * @return True if the driver is already connected to the database.
     */
    public boolean isConnected() {
        if (connection != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Directly executes an sql command.
     * @param sql an sql command String.
     */
    public void execute(String sql) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException ex) {
            if (!ex.getSQLState().equals("X0Y32") && !ex.getSQLState().equals("42Y55")) {
                System.out.println(ex.getSQLState());
                System.err.println("BUGGY : " + sql);
                throw new RuntimeException(ex);
            }

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new RuntimeException("Could not close the SQL statement!", ex);
                }
            }
        }
    }

    /**
     * Creates a new prepared statement on the database given an sql command.
     * @param sql a prepared statement sql String command.
     * @return a new PreparedStatement for the given sql command.
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    /**
     * Creates a new empty statement on the database.
     * @return a new empty Statement.
     * @throws SQLException
     */
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * Creates a new Blob object on the database.
     * @return a new empty Blob object.
     * @throws SQLException
     */
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        DbConnector other = (DbConnector) obj;
        if (other.getDbName().equals(this.getDbName())
                && other.getUser().equals(this.getUser())
                && other.getUrlBase().equals(this.getUrlBase())
                && other.getDriverHome().equals(this.getDriverHome())
                && other.getHost().equals(this.getHost())
                && other.getPort() == this.getPort()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 67 * hash + (this.dbName != null ? this.dbName.hashCode() : 0);
        hash = 67 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 67 * hash + this.port;
        return hash;
    }
}
