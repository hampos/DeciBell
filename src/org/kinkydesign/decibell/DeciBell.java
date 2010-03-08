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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.kinkydesign.decibell.core.Component;
import org.kinkydesign.decibell.interfaces.JDeciBell;
import org.reflections.Reflections;
/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeciBell implements JDeciBell{

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

    Set<Class<? extends Component>> components = null;

    public void attach(Class c) {
        c.asSubclass(Component.class);
        if(components == null){
            components = new HashSet<Class<? extends Component>>();
        }
        this.components.add(c);

    }

    public void start(){

        if(this.components == null){
            Reflections reflections = new Reflections("");
            components = reflections.getSubTypesOf(Component.class);
        }

        for(Class c : components){
            System.out.println(c);
        }


    }

    public void restart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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


  
}
