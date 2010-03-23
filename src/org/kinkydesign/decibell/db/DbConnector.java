package org.kinkydesign.decibell.db;

import java.sql.Connection;
import org.kinkydesign.decibell.db.interfaces.JDbConnector;

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
    private String javaOptions ="-Djava.net.preferIPv4Stack=true";
    private String driverHome = "/usr/local/sges-v3/javadb";
    private String host = "localhost";
    private int port = 1527;
    private String databaseUrl = urlBase+host+":"+port+"/"+dbName+";user="+user;

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

    public void connect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInitialized() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}