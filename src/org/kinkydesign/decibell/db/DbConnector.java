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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.kinkydesign.decibell.db.interfaces.JDbConnector;
import org.apache.derby.jdbc.ClientDataSource;
import org.kinkydesign.decibell.collections.ComponentRegistry;
import org.kinkydesign.decibell.db.table.Table;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DbConnector implements JDbConnector {

    private String user = "itsme";
    private String password = "letmein";
    private String urlBase = "jdbc:derby://";
    private String dbName = "db";
    private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private String javacmd = "java";
    private String javaOptions = "-Djava.net.preferIPv4Stack=true";
    private String driverHome = "/usr/local/sges-v3/javadb";
    private String host = "localhost";
    private int port = 1527;
    private String databaseUrl = urlBase + host + ":" + port + "/" + dbName + ";user=" + user;
    private String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    private Connection connection;
    private final Runtime runtime = Runtime.getRuntime();

    public void setDriverHome(String driverHome) {
        this.driverHome = driverHome;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public String getDriver() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUser() {
        return user;
    }

    public void setDbPort(int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDbPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    
    public void connect() {
        if (!isConnected()) {
            try {
                startDerbyServer();
                loadDriver();
                establishConnection();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

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

    public void killDerby() {
        final String[] derby_kill_command = {
            javacmd, javaOptions,
            "-jar", driverHome + "/lib/derbyrun.jar", "server", "shutdown"
        };
        try {
            Runtime.getRuntime().exec(derby_kill_command);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isDerbyRunning() {
        final String[] derby_ping_command = {
            javacmd, javaOptions,
            "-jar", driverHome + "/lib/derbyrun.jar", "server", "ping",
            "-p", Integer.toString(port)};
        try {
            Process derby_ping = Runtime.getRuntime().exec(derby_ping_command);
            BufferedReader br = new BufferedReader(new InputStreamReader(derby_ping.getInputStream()));
            if (br.readLine().contains("Connection obtained")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            return false;
        }
    }

    public void startDerbyServer() throws IOException {
        /**
         * We tried the following, but encountered some problems. In particular it
         * was impossible to connect to the database from the console when the
         * application was connected already!
         */
        final String[] derby_start_command = {
            javacmd,
            javaOptions,
            "-jar", driverHome + "/lib/derbyrun.jar", "server", "start",
            "-p", Integer.toString(port),};
        boolean derby_alive = isDerbyRunning();
        while (!derby_alive) {
            runtime.exec(derby_start_command);
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
            }
            derby_alive = isDerbyRunning();
        }
    }

    /**
     * Load the JDBC driver specified in the properties section
     *
     * @see Configuration server configuration
     * @see Configuration#getProperties() current properties
     * @see Configuration#loadDefaultProperties() default properties
     */
    private void loadDriver() {
        try {
            Driver myDriver = (Driver) Class.forName(databaseDriver).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Establishes a connection with the database or creates a new database
     * if the specified is not found.
     */
    private void establishConnection() {
        Properties databaseConnectionProps = new Properties();
        databaseConnectionProps.setProperty("user", user);
        databaseConnectionProps.setProperty("password", password);
        databaseConnectionProps.setProperty("securityMechanism", Integer.toString(ClientDataSource.STRONG_PASSWORD_SUBSTITUTE_SECURITY));
        try {
            connection = DriverManager.getConnection(databaseUrl, databaseConnectionProps);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 40000) {
                createDataBase();
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * This method is called when the specified database does not exist and it is
     * created. The directive <code>create=true</code> is used within the URL
     * of the database
     */
    private void createDataBase() {
        try {
            connection = DriverManager.getConnection(databaseUrl + ";create=true", user, password);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void execute(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException ex) {
            if(!ex.getSQLState().equals("X0Y32") && !ex.getSQLState().equals("42Y55") ){
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

    public void clearDB() {
        ComponentRegistry registry = new ComponentRegistry(this);
        Object[] tables = registry.getRelationTables(this).toArray();
        for(int i=tables.length-1 ; i>=0 ; i--){
            Table t = (Table)tables[i];
            execute("DROP TABLE "+t.getTableName());
        }
        tables = registry.get(this).values().toArray();
        for(int i=tables.length-1 ; i>=0 ; i--){
            Table t = (Table)tables[i];
            execute("DROP TABLE "+t.getTableName());
        }
        execute("DROP SCHEMA "+user+" RESTRICT");
    }
}
