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
package org.kinkydesign.decibell.db.query;

import org.kinkydesign.decibell.collections.*;
import org.kinkydesign.decibell.db.interfaces.*;
import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 *
 * An equality or inequality proposition consitis of three parts: The parameter (the
 * subject) which is a table column, the qualifier (a relation defined in {@link Qualifier }
 * which is the predicate ) and the value (the object).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Proposition
        implements Cloneable {

    private JTableColumn tableColumn;
    private Qualifier qualifier;
    private String stringValue = null;
    /**
     * (100) corresponds to integer-type, (010) to numeric type and
     * (001) to string type.
     */
    private boolean[] columnType = new boolean[3];

    /**
     * Construct a new Proposition.
     */
    public Proposition() {
    }

    public Proposition(final Proposition other){
        this.tableColumn = other.tableColumn;
        this.qualifier = other.qualifier;
        this.stringValue = other.stringValue;
        this.columnType = other.columnType;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Qualifier getQualifier() {
        return qualifier;
    }

    public void setQualifier(Qualifier qualifier) {
        this.qualifier = qualifier;
    }

    public JTableColumn getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(JTableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    public void setInt(int intValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.INTEGER)) ) {
            throw new IllegalArgumentException("Cannot set an integer/long value to a non-integer type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[0] = true;
        stringValue = Integer.toString(intValue);
    }

    public void setShort(short shortValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.SMALLINT))) {
            throw new IllegalArgumentException("Cannot set an integer/long value to a non-integer type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[0] = true;
        stringValue = Short.toString(shortValue);
    }

    public void setLong(long longValue) throws IllegalArgumentException {
        if (!tableColumn.getColumnType().equals(SQLType.BIGINT)) {
            throw new IllegalArgumentException("Cannot set an integer/long value to a non-integer type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[0] = true;
        stringValue = Long.toString(longValue);
    }

    public void setDouble(double numericValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.DOUBLE)) && (!tableColumn.getColumnType().equals(SQLType.DECIMAL))) {
            throw new IllegalArgumentException("Cannot set a numeric value to a non-numeric type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[1] = true;
        stringValue = Double.toString(numericValue);
    }

    public void setFloat(float numericValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.REAL)) && (!tableColumn.getColumnType().equals(SQLType.DOUBLE))) {
            throw new IllegalArgumentException("Cannot set a numeric value to a non-numeric type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[1] = true;
        stringValue = Float.toString(numericValue);
    }

    public void setString(String stringValue) {
        if ((!tableColumn.getColumnType().equals(SQLType.CHAR)) && (!tableColumn.getColumnType().equals(SQLType.VARCHAR)) && (!tableColumn.getColumnType().equals(SQLType.LONG_VARCHAR))) {
            throw new IllegalArgumentException("Cannot set a string value to a non-string type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[2] = true;
        this.stringValue = "\'" + stringValue + "\'";
    }

    public void setNull() throws IllegalArgumentException {
        if (tableColumn.isNotNull()) {
            throw new IllegalArgumentException("Cannot set to NULL the non-nullable column " + tableColumn.getColumnName());
        }
        this.stringValue = "NULL";
        columnType = new boolean[3];
    }


    public void setQueryValue(SQLQuery queryValue){
        setQualifier(Qualifier.IN);
        this.stringValue = "( "+ queryValue.getSQL() + ")";
    }

    public void setUnknown() {
        this.stringValue = QUESTION_MARK;
    }

    @Override
    public String toString() {

        boolean qualifierForNull = (qualifier == Qualifier.IS || qualifier == Qualifier.IS_NOT);

        

        if (qualifierForNull && (columnType[0] || columnType[1] || columnType[2])) {
            throw new IllegalArgumentException("Illegal Qualifier {" + qualifier + "} combined with not null value (Read Derby Manual)");
        }

        StringBuffer string = new StringBuffer();
        string.append(tableColumn.getFullName());
        string.append(SPACE);
        string.append(qualifier.toString());
        string.append(SPACE);
        string.append(stringValue);
        return string.toString();
    }
}
