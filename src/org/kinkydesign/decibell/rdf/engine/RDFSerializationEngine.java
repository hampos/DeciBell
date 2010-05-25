/**
 *  Class : RDFSerializationEngine
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
package org.kinkydesign.decibell.rdf.engine;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Collection;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.derby.DerbyTable;
import org.kinkydesign.decibell.db.interfaces.*;
import org.kinkydesign.decibell.rdf.ns.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RDFSerializationEngine {

    private static Individual createFieldIndividual(Field input, OntObject oo) {
        String fieldName = null;
        try {
            fieldName = URLEncoder.encode(input.getName(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            fieldName = input.getName();
            System.err.println("Could not encode the string : '" + input.getName() + "' using UTF-8");
            ex.printStackTrace();
        }
        Individual fieldIndividual = oo.createIndividual(OntEntity.DECIBELL_BASE + "field_" + fieldName,
                DBClass.field().getResource());
        fieldIndividual.addLiteral(DBDataTypeProperties.fName(oo),
                oo.createTypedLiteral(input.getName(), XSDDatatype.XSDstring));
        fieldIndividual.addLiteral(DBDataTypeProperties.fieldClass(oo),
                oo.createTypedLiteral(input.getDeclaringClass().getName(), XSDDatatype.XSDstring));
        fieldIndividual.addLiteral(DBDataTypeProperties.fieldType(oo),
                oo.createTypedLiteral(input.getGenericType(), XSDDatatype.XSDstring));
        for (Annotation annotation : input.getAnnotations()) {
            fieldIndividual.addLiteral(DBDataTypeProperties.fieldAnnotation(oo),
                    oo.createTypedLiteral(annotation.annotationType().getName(), XSDDatatype.XSDstring));
        }
        return fieldIndividual;
    }

    private static Individual createConstraintIndividual(JTableColumn input, OntObject oo) {
        String columnName = null;
        try {
            columnName = URLEncoder.encode(input.getColumnName(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            columnName = input.getColumnName();
            System.err.println("Could not encode the string : '" + input.getColumnName() + "' using UTF-8");
            ex.printStackTrace();
        }
        Individual constraintIndiv = oo.createIndividual(OntEntity.DECIBELL_BASE + "constraint_" + columnName,
                DBClass.constraint().getResource());
        if (input.hasHigh()) {
            constraintIndiv.addLiteral(DBDataTypeProperties.higherBound(oo), oo.createTypedLiteral(input.getHigh(), XSDDatatype.XSDdouble));
        }
        if (input.hasLow()) {
            constraintIndiv.addLiteral(DBDataTypeProperties.lowerBound(oo), oo.createTypedLiteral(input.getLow(), XSDDatatype.XSDdouble));
        }
        if (input.hasDomain()) {
            for (String domainElement : input.getDomain()) {
                constraintIndiv.addLiteral(DBDataTypeProperties.acceptsValue(oo), oo.createTypedLiteral(domainElement, XSDDatatype.XSDstring));
            }
        }
        return constraintIndiv;
    }

    private static Individual createTableColumnIndividual(JTableColumn input, OntObject oo) {
        String columnName = null;
        try {
            columnName = URLEncoder.encode(input.getColumnName(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            columnName = input.getColumnName();
            System.err.println("Could not encode the string : '" + input.getColumnName() + "' using UTF-8");
            ex.printStackTrace();
        }
        Individual tableColumnIndividual = oo.createIndividual(OntEntity.DECIBELL_BASE + "column_" + columnName,
                DBClass.tableColumn().getResource());

        tableColumnIndividual.addLiteral(DBDataTypeProperties.tcName(oo),
                oo.createTypedLiteral(input.getColumnName(), XSDDatatype.XSDstring));

        tableColumnIndividual.addLiteral(DBDataTypeProperties.hasDomain(oo),
                oo.createTypedLiteral(input.hasDomain(), XSDDatatype.XSDboolean));
        tableColumnIndividual.addLiteral(DBDataTypeProperties.hasHigh(oo),
                oo.createTypedLiteral(input.hasHigh(), XSDDatatype.XSDboolean));
        tableColumnIndividual.addLiteral(DBDataTypeProperties.hasLow(oo),
                oo.createTypedLiteral(input.hasLow(), XSDDatatype.XSDboolean));
        tableColumnIndividual.addLiteral(DBDataTypeProperties.isForeignKey(oo),
                oo.createTypedLiteral(input.isForeignKey(), XSDDatatype.XSDboolean));
        if (input.isTypeNumeric()) {
            tableColumnIndividual.addLiteral(DBDataTypeProperties.isAutoGen(oo),
                    oo.createTypedLiteral(input.isAutoGenerated(), XSDDatatype.XSDboolean));
        }
        tableColumnIndividual.addLiteral(DBDataTypeProperties.isPrimaryKey(oo),
                oo.createTypedLiteral(input.isPrimaryKey(), XSDDatatype.XSDboolean));
        tableColumnIndividual.addLiteral(DBDataTypeProperties.isNotNull(oo),
                oo.createTypedLiteral(input.isNotNull(), XSDDatatype.XSDboolean));
        tableColumnIndividual.addLiteral(DBDataTypeProperties.isUnique(oo),
                oo.createTypedLiteral(input.isUnique(), XSDDatatype.XSDboolean));
        if (input.hasDefault()) {
            tableColumnIndividual.addLiteral(DBDataTypeProperties.defaultValue(oo),
                    oo.createTypedLiteral(input.getDefaultValue(), XSDDatatype.XSDstring));
        }
        if (input.isTypeNumeric() && input.getNumericNull() != null) {
            tableColumnIndividual.addLiteral(DBDataTypeProperties.numericNull(oo),
                    oo.createTypedLiteral(input.getNumericNull(), XSDDatatype.XSDdouble));
        }


        Individual dataTypeIndividual = DBSQLTypes.fromSQLTypes(input.getColumnType(), oo);

        tableColumnIndividual.addProperty(DBObjectProperties.hasSQLType(oo), dataTypeIndividual);

        if (input.isForeignKey()) {
            if (!input.isSelfReferencing()) {
                tableColumnIndividual.addProperty(DBObjectProperties.columnReferencesColumn(oo),
                        createTableColumnIndividual(input.getReferenceColumn(), oo));
            } else {
                tableColumnIndividual.addProperty(DBObjectProperties.columnReferencesColumn(oo), tableColumnIndividual);
            }
            tableColumnIndividual.addLiteral(DBDataTypeProperties.onDelete(oo),
                    oo.createTypedLiteral(input.getOnDelete().toString(), XSDDatatype.XSDstring));
            tableColumnIndividual.addLiteral(DBDataTypeProperties.onUpdate(oo),
                    oo.createTypedLiteral(input.getOnUpdate().toString(), XSDDatatype.XSDstring));
        }

        if (input.isConstrained()) {
            tableColumnIndividual.addProperty(DBObjectProperties.hasConstraint(oo), createConstraintIndividual(input, oo));
        }

        tableColumnIndividual.addProperty(DBObjectProperties.hasField(oo), createFieldIndividual(input.getField(), oo));

        return tableColumnIndividual;
    }

    private static Individual createTableIndividual(JTable input, OntObject oo) {
        String tableName = null;
        try {
            tableName = URLEncoder.encode(input.getTableName(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            tableName = input.getTableName();
            System.err.println("Could not encode the string : '" + input.getTableName() + "' using UTF-8");
            ex.printStackTrace();
        }
        Individual tableIndividual = oo.createIndividual(OntEntity.DECIBELL_BASE + "table_" + tableName,
                DBClass.table().getResource());

        tableIndividual.addLiteral(DBDataTypeProperties.tName(oo),
                oo.createTypedLiteral(input.getTableName(), XSDDatatype.XSDstring));

        for (JTableColumn tableColumn : input.getTableColumns()) {
            tableIndividual.addProperty(DBObjectProperties.hasColumn(oo), createTableColumnIndividual(tableColumn, oo));
        }

        return tableIndividual;
    }

    private static Individual createSchemaIndividual(ComponentRegistry input, OntObject oo) {
        Individual schemaIndiv = oo.createIndividual(OntEntity.DECIBELL_BASE + "schema", DBClass.schema().getResource());
        boolean schemaNameAdded = false;
        for (JTable table : input.values()) {
            if (!schemaNameAdded) {
                schemaIndiv.addLiteral(DBDataTypeProperties.schemaName(oo),
                        oo.createTypedLiteral(table.getTableSchema(), XSDDatatype.XSDstring));
                schemaNameAdded = true;
            }
            schemaIndiv.addProperty(DBObjectProperties.schemaTable(oo), createTableIndividual(table, oo));
        }
        return schemaIndiv;

    }

    public static OntObject serialize(JTableColumn input) {
        OntObject oo = new OntObject();
        oo.includeOntClasses(DBClass.tableColumn(), DBClass.sqlType(), DBClass.field(), DBClass.constraint());
        createTableColumnIndividual(input, oo);
        return oo;
    }

    public static OntObject serialize(JTable input) {
        OntObject oo = new OntObject();
        oo.includeOntClasses(DBClass.table(), DBClass.tableColumn(), DBClass.sqlType(), DBClass.field(), DBClass.constraint());
        createTableIndividual(input, oo);
        return oo;
    }

    public static OntObject serialize(ComponentRegistry input) {
        OntObject oo = new OntObject();
        oo.includeOntClasses(DBClass.table(), DBClass.tableColumn(), DBClass.sqlType(), DBClass.field(), DBClass.constraint(), DBClass.schema());
        createSchemaIndividual(input, oo);
        return oo;
    }

    public static void main(String... args) {
        JTableColumn tc = new TableColumn("cxxd");
        tc.setColumnType(SQLType.BIGINT);
        tc.setNumericNull("4");
        tc.setDomain(new String[]{"X", "Y"});
        tc.setDefaultValue("54");
        tc.setField(DeciBell.class.getDeclaredFields()[1]);

        DerbyTable dt = new DerbyTable();
        dt.setTableName("itsme", "dt");
        dt.addColumn(tc);
        serialize(tc).printConsole();
    }
}
