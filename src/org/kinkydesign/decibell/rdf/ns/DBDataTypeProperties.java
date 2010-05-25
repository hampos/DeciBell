/**
 *  Class : DBDataTypeProperties
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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * <p  align="justify" style="width:60%">
 * Collection of datatype properties used in DeciBell. These properties are used
 * for the creation of a structured data model that fully describes the table structure,
 * provides all necessary metadata for all tables in the database and all necessary
 * meta information for the table columns therein. This renders the table structure
 * fully portable and self-describing.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DBDataTypeProperties extends OntEntity {

    /**
     * <p  align="justify" style="width:60%">
     * Datatype property that applies on table columns and its values 
     * are of type {@link XSDDatatype#XSDstring xsd:string}. Connects a
     * table columns with its default value.
     * </p>
     * @param model
     *      Ontological Model in which this property is to be encapsulated.
     * @return
     *      Datatype property for the default value of a table column.
     */
    public static final DatatypeProperty defaultValue(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "defaultValue"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Datatype property that applies on table columns and its values
     * are of type {@link XSDDatatype#XSDboolean xsd:boolean}. Assigns a boolean
     * value to a table column according to whether it has a specified domain of
     * acceptable values.
     * </p>
     * @param model
     *      Ontological Model in which this property is to be encapsulated.
     * @return
     *      Datatype property telling whether there is a specified domain
     *      for the table column.
     * @see DBDataTypeProperties#acceptsValue(org.kinkydesign.decibell.rdf.ns.OntObject) accepts value
     */
    public static final DatatypeProperty hasDomain(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "hasDomain"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Datatype property that applies on table columns and its values
     * are of type {@link XSDDatatype#XSDboolean xsd:boolean}. Assigns a boolean
     * value to a table column according to whether it has a specified higher
     * bound.
     * </p>
     * @param model
     *      Ontological Model in which this property is to be encapsulated.
     * @return
     *      Datatype property telling whether there is a higher
     *      bound constraint imposed on the table column.
     */
    public static final DatatypeProperty hasHigh(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "hasHigh"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty hasLow(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "hasLow"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty isAutoGen(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "isAutoGenerated"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty isConstrained(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "isConstrained"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty isForeignKey(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "isForeignKey"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty isNotNull(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "isNotNullable"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty isPrimaryKey(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "isPrimaryKey"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty isUnique(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "isUnique"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xboolean);
        return property;
    }

    public static final DatatypeProperty numericNull(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "numericNull"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xdouble);
        return property;
    }

    public static final DatatypeProperty onDelete(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "onDelete"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty onUpdate(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "onUpdate"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty tcName(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "tableColumnName"));
        property.addDomain(DBClass.TableColumn().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty tName(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "tableName"));
        property.addDomain(DBClass.Table().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty fName(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "fieldName"));
        property.addDomain(DBClass.Field().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty fieldClass(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "fieldDeclaringClass"));
        property.addDomain(DBClass.Field().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty fieldType(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "fieldType"));
        property.addDomain(DBClass.Field().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty fieldAnnotation(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "fieldAnnotation"));
        property.addDomain(DBClass.Field().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty lowerBound(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "lowerBound"));
        property.addDomain(DBClass.Constraint().getResource());
        property.addRange(XSD.xdouble);
        return property;
    }

    public static final DatatypeProperty higherBound(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "higherBound"));
        property.addDomain(DBClass.Constraint().getResource());
        property.addRange(XSD.xdouble);
        return property;
    }

    public static final DatatypeProperty acceptsValue(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "acceptsValue"));
        property.addDomain(DBClass.Constraint().getResource());
        property.addRange(XSD.xstring);
        return property;
    }

    public static final DatatypeProperty schemaName(OntObject model) {
        DatatypeProperty property = model.createDatatypeProperty(String.format(_DECIBELL_BASE, "schemaName"));
        property.addDomain(DBClass.Table().getResource());
        property.addRange(XSD.xstring);
        return property;
    }
}
