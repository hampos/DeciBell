package org.kinkydesign.decibell.db.interfaces;

import java.sql.Connection;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JDbConnector {

    void setDriverHome(String driverHome);

    void setDriver(String driver);
    String getDriver();

    void setJavaOptions(String javaOptions);

    void setJavacmd(String javacmd);

    void setUser(String user);
    String getUser();

    void setDbPort(int port);
    int getDbPort();


    /**
     * Starts a new connection with the database. The connection
     * URL and username are found at the properties file of the server. If already
     * connected to the database, no action is taken.
     */
    void connect();

    /**
     * Disconnects the database.
     */
    void disconnect();

    /**
     * Returns an SQL Connection object.
     * @return connection with the database - null if no connection is alive.
     */
    Connection getConnection();

    /**
     * Retrieves whether the database connection is properly initialized, i.e.
     * if there is an established connection and the standard tables where
     * created.
     * @return <code>true</code> if the connector is initialized.
     */
    boolean isInitialized();

    boolean isConnected();



}
