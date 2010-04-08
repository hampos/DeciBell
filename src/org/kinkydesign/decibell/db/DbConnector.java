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
 * A Connector to a database identified by its URL.
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

    public DbConnector() {
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDriverHome() {
        return driverHome;
    }

    public String getHost() {
        return host;
    }

    public String getJavaOptions() {
        return javaOptions;
    }

    public String getJavacmd() {
        return javacmd;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public Runtime getRuntime() {
        return runtime;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public String getUser() {
        return user;
    }

    public String getDatabaseUrl() {
        return urlBase + host + ":" + port + "/" + dbName + ";user=" + user;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setDriverHome(String driverHome) {
        this.driverHome = driverHome;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setJavaOptions(String javaOptions) {
        this.javaOptions = javaOptions;
    }

    public void setJavacmd(String javacmd) {
        this.javacmd = javacmd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public abstract void connect();

    public abstract void killServer();

    public abstract boolean isServerRunning();

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

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        if (connection != null) {
            return true;
        } else {
            return false;
        }
    }

    public void execute(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException ex) {
            if (!ex.getSQLState().equals("X0Y32") && !ex.getSQLState().equals("42Y55")) {
                System.out.println(ex.getSQLState());
                throw new RuntimeException(ex);
            }
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }
}
