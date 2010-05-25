/**
 *  Class : DBClass
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

/**
 * <p  align="justify" style="width:60%">
 * Collection of ontological classes used in DeciBell an standing as datatypes for
 * the main conceptions of DeciBell which are the Table, the TableColumn and the Field
 * (and some other auxiliary classes).
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DBClass extends OntEntity {

    /**
     * <p  align="justify" style="width:60%">
     * Construct a new <code>DBClass</code> (DeciBell ontological class) providing
     * the resource that describes the class and holds all necessary meta-information
     * for it. In ontological terms, every DeciBell class is uniquely identified by
     * its <code>URI</code> which is provided as a <code>Resource</code>.
     * </p>
     * @param resource
     *      Resource describing db-class meta information.
     */
    public DBClass(Resource resource) {
        super(resource);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Construct a new <code>DBClass</code> providing just the name of the resource; the
     * URI of the resource is internall generated using the standard namespace that all
     * DeciBell entities share. For example, given the resource name 'Table', the URI
     * of the created <code>DBClass</code> will be something like
     * <code>http://www.kinkydesign.org/decibell/ver.0.1/#Table</code>.
     * </p>
     * @param resourceName
     *      The name of the resource as a String.
     */
    public DBClass(String resourceName) {
        this(model.createResource(String.format(_DECIBELL_BASE, resourceName)));
    }

    /**
     * <p  align="justify" style="width:60%">
     * Construct a new <code>DBClass</code> object with no resource specified.
     * </p>
     */
    public DBClass() {
        super();
    }

    /**
     * <p  align="justify" style="width:60%">
     * The ontological entity for DeciBell tables.
     * </p>
     * @return
     *      The <code>DBClass</code> corresponding to DeciBell table entities.
     */
    public static final DBClass Table() {
        DBClass dbc = new DBClass("Table");
        return dbc;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The ontological entity for DeciBell table columns.
     * </p>
     * @return
     *      The <code>DBClass</code> corresponding to DeciBell table column entities.
     */
    public static final DBClass TableColumn() {
        DBClass dbc = new DBClass("TableColumn");
        return dbc;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The ontological entity for constraints imposed on DeciBell table columns.
     * </p>
     * @return
     *      The <code>DBClass</code> corresponding to table column constraints.
     */
    public static final DBClass Constraint() {
        DBClass dbc = new DBClass("Constraint");
        return dbc;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The ontological entity for fields of the Java<sup><tt>TM</tt></sup> code which
     * are used by DeciBell to generate one or more table columns.
     * </p>
     * @return
     *      The <code>DBClass</code> corresponding to Java<sup><tt>TM</tt></sup> fields.
     */
    public static final DBClass Field() {
        DBClass dbc = new DBClass("Field");
        return dbc;
    }

    /**
     * <p  align="justify" style="width:60%">
     * The ontological entity for SQL Types. A list of some available SQLTypes
     * is found in {@link DBSQLTypes }.
     * </p>
     * @return
     *      The <code>DBClass</code> corresponding to the notion of an SQLType.
     */
    public static final DBClass SQLType() {
        DBClass dbc = new DBClass("SQLType");
        return dbc;
    }
}
