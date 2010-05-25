/**
 *  Class : DBSQLTypes
 *  Date  : May 24, 2010
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
package org.kinkydesign.decibell.rdf.ns;

import com.hp.hpl.jena.rdf.model.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.kinkydesign.decibell.collections.SQLType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DBSQLTypes extends OntEntity {

    public DBSQLTypes(Resource resource) {
        super(resource);
    }

    public DBSQLTypes() {
        super();
    }

    public DBSQLTypes(String resourceName) {
        this(model.createResource(String.format(_DECIBELL_DATATYPES, resourceName)));
    }

    /**
     * Ontological entity that corresponds to some given {@link SQLType }.
     * @param type
     *      An {@link SQLType } for which the corresponding ontological resource is needed.
     * @return
     *      An ontological resource wrapped as an instance of {@link DBSQLTypes }.
     */
    public static final DBSQLTypes fromSQLTypes(SQLType type) {
        String name = "";
        try {
            name = URLEncoder.encode(type.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            name = type.toString();
            System.err.println("[WARNING] UTF-8 encoding error for " + name);
            ex.printStackTrace();
        }
        DBSQLTypes dbc = new DBSQLTypes(name);
        return dbc;
    }
}
