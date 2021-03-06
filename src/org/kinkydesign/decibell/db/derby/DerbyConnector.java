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
package org.kinkydesign.decibell.db.derby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDataSource;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.Table;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DerbyConnector extends DbConnector {

    public DerbyConnector() {
        super();
        setUser("itsme");
        setPassword("letmein");
        setUrlBase("jdbc:derby://");
        setDbName("db");
        setJavacmd("java");
        setJavaOptions("-Djava.net.preferIPv4Stack=true");
        // setDriverHome("/usr/local/sges-v3/javadb");
        if (System.getProperty("os.name").contains("Mac")) {
            setDriverHome(System.getenv("DERBY_HOME"));
        } else {
            setDriverHome(System.getProperty("user.home") + "/JLib/10.6.0.0alpha_2010-02-15T19-30-14_SVN910262");
        }
        setHost("localhost");
        setPort(1527);
        setDatabaseDriver("org.apache.derby.jdbc.EmbeddedDriver");

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

    public void killServer() {
        System.err.print("KILLING DERBY JDBC SERVER...");
        final String[] derby_kill_command = {
            getJavacmd(), getJavaOptions(),
            "-jar", getDriverHome() + "/lib/derbyrun.jar", "server", "shutdown"
        };
        try {
            Process killing = Runtime.getRuntime().exec(derby_kill_command);
            killing.waitFor();
            int N_TRIES = 5, i = 0;
            while (killing.exitValue() != 0 && i < N_TRIES) {
                Thread.sleep(1000);
                i++;
            }
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(DerbyConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        System.err.print(" DERBY IS DOWN!\n");
    }

    public boolean isServerRunning() {
        final String[] derby_ping_command = {
            getJavacmd(), getJavaOptions(),
            "-jar", getDriverHome() + "/lib/derbyrun.jar", "server", "ping",
            "-p", Integer.toString(getPort())};
        BufferedReader br = null;
        try {
            Process derby_ping = Runtime.getRuntime().exec(derby_ping_command);
            br = new BufferedReader(new InputStreamReader(derby_ping.getInputStream()));
            if (br.readLine().contains("Connection obtained")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException("Could not close the Buffered Reader!",ex);
            }
        }
    }

    public void clearDB() {
        Object[] tables = ComponentRegistry.getRegistry(this).getRelationTables().toArray();
        for (int i = tables.length - 1; i >= 0; i--) {
            Table t = (Table) tables[i];
            execute("DROP TABLE " + t.getFullTableName());
        }
        tables = ComponentRegistry.getRegistry(this).values().toArray();
        for (int i = tables.length - 1; i >= 0; i--) {
            Table t = (Table) tables[i];
            execute("DROP TABLE " + t.getFullTableName());
        }
        execute("DROP TABLE " + getUser() + ".DECIBELL_INIT_TAB");
        execute("DROP SCHEMA " + getUser() + " RESTRICT");
    }

    private void startDerbyServer() throws IOException {
        /**
         * We tried the following, but encountered some problems. In particular it
         * was impossible to connect to the database from the console when the
         * application was connected already!
         */
        final String[] derby_start_command = {
            getJavacmd(),
            getJavaOptions(),
            "-jar", getDriverHome() + "/lib/derbyrun.jar", "server", "start",
            "-p", Integer.toString(getPort()),};
        boolean derby_alive = isServerRunning();
        while (!derby_alive) {
            getRuntime().exec(derby_start_command);
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
            }
            derby_alive = isServerRunning();
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
            Driver myDriver = (Driver) Class.forName(getDatabaseDriver()).newInstance();
            if (!myDriver.jdbcCompliant()) {
                throw new RuntimeException("NON-JDBC compliant driver!");
            }
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
        int NUMBER_TRIES = 10;
        int i = 0;
        Properties databaseConnectionProps = new Properties();
        databaseConnectionProps.setProperty("user", getUser());
        databaseConnectionProps.setProperty("password", getPassword());
        databaseConnectionProps.setProperty("securityMechanism", Integer.toString(ClientDataSource.STRONG_PASSWORD_SUBSTITUTE_SECURITY));
        try {
            setConnection(DriverManager.getConnection(getDatabaseUrl(), databaseConnectionProps));
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 40000) {
                createDataBase();
            } else if (ex.getSQLState().equals("XJ040") && ex.getErrorCode() == -1 && i < NUMBER_TRIES) {
                /*
                 * Waiting for other users to log off so that you can
                 * manage to log in.
                 */
                try {
                    System.out.println("[INFO] Waiting for another instance of Derby to log out. Please wait...");
                    Thread.sleep(1000);
                    i++;
                    establishConnection();
                } catch (InterruptedException ex1) {
                    Logger.getLogger(DerbyConnector.class.getName()).log(Level.SEVERE, null, ex1);
                }
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
            setConnection(DriverManager.getConnection(getDatabaseUrl() + ";create=true", getUser(), getPassword()));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
