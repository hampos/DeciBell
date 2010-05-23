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

    /**
     * The table column which is the subject of the proposition.
     */
    private JTableColumn tableColumn;
    /**
     * The qualifier used in the proposition.
     * @see Qualifier
     */
    private Qualifier qualifier;
    /**
     * The value of a simple proposition (a proposition which accepts a <code>value</code>
     * that is not some {@link SQLQuery } but a primitive value) as a String.
     */
    private String stringValue = null;
    /**
     * The value of a nob-simple proposition, that is some SQLQuery.
     */
    private SQLQuery sqlQueryValue = null;
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

    /**
     * Construct a copy of some other proposition.
     * @param other
     */
    public Proposition(final Proposition other) {
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

    /**
     * The qualifier of the proposition
     * @return
     *      Qualifier.
     */
    public Qualifier getQualifier() {
        return qualifier;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Define the qualifier of the proposition. If the proposition is non-simple, i.e.
     * its value is some {@link SQLQuery } then the qualifier is automatically set to
     * {@link Qualifier#IN IN}. This happens once you invoke the method
     * {@link Proposition#setQueryValue(org.kinkydesign.decibell.db.query.SQLQuery) setQueryValue}.
     * </p>
     * @param qualifier
     *
     */
    public void setQualifier(Qualifier qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * Get the table column which is the subject of the proposition
     * @return
     *      Proposition's subject (table column).
     */
    public JTableColumn getTableColumn() {
        return tableColumn;
    }

    /**
     * Provide a table column for the proposition.
     * @param tableColumn
     *      A table column which is the subject of the poropositon.
     */
    public void setTableColumn(JTableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    /**
     * Set an <code>integer</code> value to the proposition.
     * @param intValue
     *      Integer value.
     * @throws IllegalArgumentException
     *      In case the underlying table column is not of propoer type (is not {@link SQLType#INTEGER integer}).
     */
    public void setInt(int intValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.INTEGER))) {
            throw new IllegalArgumentException("Cannot set an integer/long value to a non-integer type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[0] = true;
        stringValue = Integer.toString(intValue);
    }

    /**
     * Set a <code>short</code> value to the proposition.
     * @param shortValue
     *      Short value.
     * @throws IllegalArgumentException
     *      In case the underlying table column is not of propoer type (is not {@link SQLType#SMALLINT smallint}).
     */
    public void setShort(short shortValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.SMALLINT))) {
            throw new IllegalArgumentException("Cannot set an integer/long value to a non-integer type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[0] = true;
        stringValue = Short.toString(shortValue);
    }

    /**
     * Set a <code>long</code> value to the proposition.
     * @param longValue
     *      Long (BigInt) value.
     * @throws IllegalArgumentException
     *      In case the underlying table column is not of propoer type (is not {@link SQLType#BIGINT bigint}).
     */
    public void setLong(long longValue) throws IllegalArgumentException {
        if (!tableColumn.getColumnType().equals(SQLType.BIGINT)) {
            throw new IllegalArgumentException("Cannot set an integer/long value to a non-integer type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[0] = true;
        stringValue = Long.toString(longValue);
    }

    /**
     * Set a <code>double</code> value to the proposition.
     * @param numericValue
     *      Double value.
     * @throws IllegalArgumentException
     *      In case the underlying table column is not of 
     *      propoer type (is not {@link SQLType#DOUBLE double} or
     *      {@link SQLType#DECIMAL decimal}).
     */
    public void setDouble(double numericValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.DOUBLE)) && (!tableColumn.getColumnType().equals(SQLType.DECIMAL))) {
            throw new IllegalArgumentException("Cannot set a numeric value to a non-numeric type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[1] = true;
        stringValue = Double.toString(numericValue);
    }

    /**
     * Set a <code>float</code> value to the proposition.
     * @param numericValue
     *      Float value.
     * @throws IllegalArgumentException
     *      In case the underlying table column is not of propoer type
     *      (is not {@link SQLType#DOUBLE double} or {@link SQLType#REAL real}).
     */
    public void setFloat(float numericValue) throws IllegalArgumentException {
        if ((!tableColumn.getColumnType().equals(SQLType.REAL)) && (!tableColumn.getColumnType().equals(SQLType.DOUBLE))) {
            throw new IllegalArgumentException("Cannot set a numeric value to a non-numeric type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[1] = true;
        stringValue = Float.toString(numericValue);
    }

    /**
     * Set a <code>String</code> value to the proposition.
     * @param stringValue
     *      String value.
     * @throws IllegalArgumentException
     *      In case the underlying table column is not of
     *      propoer type (is not {@link SQLType#CHAR char} or
     *      {@link SQLType#VARCHAR varchar} or {@link SQLType#LONG_VARCHAR long varchar}).
     */
    public void setString(String stringValue) {
        if ((!tableColumn.getColumnType().equals(SQLType.CHAR)) && (!tableColumn.getColumnType().equals(SQLType.VARCHAR)) && (!tableColumn.getColumnType().equals(SQLType.LONG_VARCHAR))) {
            throw new IllegalArgumentException("Cannot set a string value to a non-string type table column."
                    + "Table Column: " + tableColumn.getColumnName() + " with type: " + tableColumn.getColumnType());
        }
        columnType[2] = true;
        this.stringValue = "\'" + stringValue + "\'";
    }

    /**
     * Set the value of the proposition to <code>null</code>
     * @throws IllegalArgumentException
     *      In case the underlying table column is not nullable.
     * @see JTableColumn#setNotNull(boolean) setNotNull in JTableColumn
     */
    public void setNull() throws IllegalArgumentException {
        if (tableColumn.isNotNull()) {
            throw new IllegalArgumentException("Cannot set to NULL the non-nullable column " + tableColumn.getColumnName());
        }
        this.stringValue = "NULL";
        columnType = new boolean[3];
    }

    /**
     * A proposition is called non-simple when it accepts an SQLQuery as its subject (value).
     * For example <code>X IN (SELECT S FROM TABLE_A WHERE S.T &gt;= ? AND S.T &lt;= ? )</code>. In
     * this example, the subject of the proposition is the table column <code>X</code>, the
     * qualifier is {@link Qualifier#IN IN} and the subject (the value) is the SQLQuery:
     * <code>SELECT S FROM TABLE_A WHERE S.T &gt;= ? AND S.T &lt;= ?</code>. Using this method
     * the qualifier is automatically set to {@link Qualifier#IN IN}.
     * @param queryValue
     *      An SQLQuery which is used as a value in the proposition.
     */
    public void setQueryValue(SQLQuery queryValue) {
        this.stringValue = null;
        setQualifier(Qualifier.IN);
        this.sqlQueryValue = queryValue;
    }

    public SQLQuery getSqlQueryValue() {
        return sqlQueryValue;
    }

    /**
     * Sets the value of the proposition to <code>?</code>.
     */
    public void setUnknown() {
        this.stringValue = QUESTION_MARK;
    }
   

    /**
     * Whether the proposition is simple, i.e. it accepts SQLQueries as its subject.
     * In fact return <code>true</code> if an SQLQuery has been previously set using the
     * method {@link Proposition#setQueryValue(org.kinkydesign.decibell.db.query.SQLQuery) setQueryValue}
     * in {@link Proposition }.
     * @return
     *      <code>true</code> if the proposition is non-simple, <code>false</code> otherwise.
     */
    public boolean isPropositionSimple() {
        if (sqlQueryValue==null){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {

        boolean qualifierForNull = (qualifier == Qualifier.IS || qualifier == Qualifier.IS_NOT);

        if (!columnType[0] && !columnType[1] && !columnType[2] && !qualifierForNull
                && (this.stringValue == null ? QUESTION_MARK != null : !this.stringValue.equals(QUESTION_MARK)) && this.sqlQueryValue == null) {
            throw new IllegalArgumentException("Use the qualifier IS or IS NOT with null");
        }

        if (qualifierForNull && (columnType[0] || columnType[1] || columnType[2])) {
            throw new IllegalArgumentException("Illegal Qualifier {" + qualifier + "} combined with not null value (Read Derby Manual)");
        }

        StringBuffer string = new StringBuffer();
        string.append(tableColumn.getFullName());
        string.append(SPACE);
        string.append(qualifier.toString());
        string.append(SPACE);
        if (sqlQueryValue != null) {
            string.append("( ");
            string.append(sqlQueryValue.getSQL());
            string.append(" )");
        } else {
            string.append(stringValue);
        }
        return string.toString();
    }
}
