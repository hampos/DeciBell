package org.kinkydesign.decibell.db.interfaces;

import java.sql.Connection;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JDbConnector {

    /**
     * Choose a name for the database you want to create or a name for an existing
     * database. Then the URL of the database connection will be
     * <code>jdbc:derby://{hostname}:{port}/{database_name}</code>. Set the database
     * connection port and hostname usign the corresponding methods.
     * @param dbName
     *      The name of the database.
     */
    void setDbName(String dbName);

    /**
     * Get the complete URL of the database connection as a String.
     * @return
     *      URL of database connection.
     */
    String getDatabaseUrl();


    /**
     * Set the full path to the Derby installation folder. The Derby manual refers
     * to this parameter as <code>$DERBY_HOME</code>.
     * @param driverHome
     *      Complete path to derby installation folder.
     */
    void setDriverHome(String driverHome);

    /**
     * Set the database driver. Choose among available drivers on your system. A well
     * known and widely used driver is <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
     * @param driver
     *      Full java-formatted path to the driver used for the connection to the
     *      database server.
     */
    void setDriver(String driver);
    /**
     * Name of the driver used for the connection to the database server.
     * @return
     *      the name of the driver
     */
    String getDriver();

    /**
     * Java options used when the derby server is started. 
     * @param javaOptions
     *      Java options for the invokation of the derby server startup command.
     */
    void setJavaOptions(String javaOptions);

    /**
     * Java command or path to it. By default is set to <code>java</code> but in some
     * cases (e.g. on Debian machines) but need to modify it to <code>/usr/bin/java</code>
     * or some other path or command.
     * @param javacmd
     *      java command
     */
    void setJavacmd(String javacmd);

    /**
     * Set the datbase connection host. By default this is set to <code>localhost</code>,
     * that is the Derby server accepts connections only localy for secutity reason, but
     * you can modify this if you are sure there are no security risks doing so. Setting
     * this field to <code>0.0.0.0</code> will allow connections from everyone, even from
     * remote clients so your database will be public!
     * @param host
     *      Database connection host.
     */
    void setHost(String host);

    /**
     * Define the user which connects to the database. Note that in <code>derby</code>
     * (The database server used by DeciBell) every user corresponds to a database
     * SCHEMA.
     * @param user
     *      Database user
     */
    void setUser(String user);

    /**
     * Get the name of the user which connects to the database.
     * @return
     *      user that connects to the database.
     */
    String getUser();

    /**
     * Set the database port.
     * @param port
     *      The port at which the connection takes place. Default is
     *      <code>1527</code>
     */
    void setDbPort(int port);

    /**
     * Get the port at which the connection takes place.
     * @return
     *      The database connection port.
     */
    int getDbPort();


    /**
     * Starts a new connection with the database. The connection
     * URL and username are found at the properties file of the server. If already
     * connected to the database, no action is taken.
     */
    void connect();

    /**
     * Shuts down the connection to the database but does not turn off the derby
     * server.
     */
    void disconnect();

    /**
     * Kills the process running the Derby server.
     */
    void killDerby();

    /**
     * Clears the database from all data. Deletes the whole database structure including
     * the tables and the user. Be careful when using this command because <b>all data will
     * be permanently lost</code>.
     */
    void clearDB();

    /**
     * Returns an SQL Connection object.
     * @return
     *      connection with the database - null if no connection is alive.
     */
    Connection getConnection();

    /**
     * Check if the connection to the database is active.
     * @return
     *      <code>true</code>, if connected to the database.
     */
    boolean isConnected();

    /**
     * Check if the Derby server is up and running
     * @return
     *      <code>true</code>, if the database server is alive, <code>false</code>
     *      otherwise.
     */
    boolean isDerbyRunning();

    /**
     * Execute an update-like SQL command given the SQL string of the command.
     * @param sql
     *      SQL command as a string.
     */
    void execute(String sql);

}
