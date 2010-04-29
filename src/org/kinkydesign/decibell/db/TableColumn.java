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
package org.kinkydesign.decibell.db;

import java.lang.reflect.Field;
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;


/**
 * A column inside a database table. The TableColumn is the heart of the sql
 * creation process. It holds all info a column needs to be created and to
 * construct the proper queries for it. It is also associated with the Component
 * class Field upon which it was created.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
final public class TableColumn implements Cloneable, JTableColumn {

    private String columnName = "";
    private SQLType columnType = null;
    private boolean isPrimaryKey = false;
    private boolean isAutoGenerated = false;
    private String defaultValue = null;
    private boolean isNotNull = false;
    private boolean isUnique = false;
    private String[] domain = null;
    private String low = null;
    private String high = null;
    private boolean isConstrained = false;
    private boolean hasLow = false;
    private boolean hasHigh = false;
    private boolean hasDomain = false;
    private boolean hasDefault = false;
    private boolean isForeignKey = false;
    private JTable referencesTable = null;
    private JTableColumn referencesColumn = null;
    private Class<? extends Component> referencesClass = null;
    private OnModification onUpdate = null;
    private OnModification onDelete = null;
    private JTable masterTable = null;
    private Field field = null;
    private String numericNull = "-1";

    /**
     * Constructs a new TableColumn object.
     */
    public TableColumn() {
        super();
    }

    /**
     * Constructs a new TableColumn object given a name for it.
     * @param columnName
     */
    public TableColumn(String columnName) {
        setColumnName(columnName);
    }

    /**
     * Returns the JTable in which the TableColumn belongs.
     * @return the JTable in which the TableColumn belongs.
     */
    public JTable getMasterTable() {
        return masterTable;
    }

    /**
     * Sets the table in which the TableColumn belongs.
     * @param masterTable the JTable the column belongs to.
     */
    public void setMasterTable(JTable masterTable) {
        this.masterTable = masterTable;
    }

    /**
     * Returns the full name of the column, in the form of: {Table name}.{TableColumn name}
     * to be used in sql syntax.
     * @return the column's full name String
     */
    public String getFullName() {
        if (masterTable == null) {
            return getColumnName();
        }
        return masterTable.getTableName() + "." + getColumnName();
    }

    /**
     * Returns the column name.
     * @return the column's name String.
     */
    public String getColumnName() {
        return this.columnName;
    }

    /**
     * Sets a new name for the column.
     * @param columnName a column name String.
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Sets a new {@link SQLType} to the column.
     * @param columnType an SQLType for the column.
     */
    public void setColumnType(SQLType columnType) {
        this.columnType = columnType;
    }

    /**
     * Returns the column's {@link SQLType}
     * @return the column's SQLType.
     */
    public SQLType getColumnType() {
        return columnType;
    }

    /**
     * Sets the column as a primary key column, and defines if it will be
     * automatically generated by the database or not.
     * @param isPrimaryKey boolean value that decides if the column is a primary key column.
     * @param isAutoGenerated boolean value that decides if the column is auto generated.
     */
    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     * Returns true if the column is a primary key column.
     * @return True if the column has isPrimaryKey=true.
     */
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * Sets the column as a foreign key column by providing the table it references,
     * the column it references, and options about deleting and updating the
     * column it references.
     * @param table the JTable that the column references.
     * @param ForeignColumn the JTableColumn that the column references.
     * @param onDelete OnModification option that decides what the database must
     * do when the referenced column row is deleted.
     * @param onUpdate OnModification option that decides what the database must
     * do when the referenced column row is updated.
     */
    public void setForeignKey(JTable table, JTableColumn ForeignColumn, OnModification onDelete, OnModification onUpdate) {
        this.isForeignKey = true;
        this.referencesTable = table;
        this.referencesColumn = ForeignColumn;
        this.onDelete = onDelete;
        this.onUpdate = onUpdate;

    }

    /**
     * Returns the name of the foreign table this column references.
     * @return the String name of this column's reference table.
     */
    public String getReferenceTableName() {
        if (referencesTable==null) return null;
        return referencesTable.getTableName();
    }

    /**
     * Returns the JTable this column references.
     * @return the JTable this column references.
     */
    public JTable getReferenceTable() {
        return referencesTable;
    }

    /**
     * Returns the name of the foreign column this column references.
     * @return the String name of the foreign JTableColumn this column references.
     */
    public String getReferenceColumnName() {
        return referencesColumn.getColumnName();
    }

    /**
     * Returns the JTableColumn this column references.
     * @return the JTableColumn this column references.
     */
    public JTableColumn getReferenceColumn() {
        return referencesColumn;
    }

    /**
     * Returns the OnModification option that decides what the database must
     * do when the foreign column row this column points to is deleted.
     * @return this column's onDelete onModification option.
     */
    public OnModification getOnDelete() {
        return onDelete;
    }

    /**
     * Returns the OnModification option that decides what the database must
     * do when the foreign column row this column points to is updated.
     * @return this column's onUpdate onModification option.
     */
    public OnModification getOnUpdate() {
        return onUpdate;
    }

    /**
     * Sets a default value to the column that would be inserted to the database
     * when no value is specified.
     * @param defaultValue a default value String.
     */
    public void setDefaultValue(String defaultValue) {
        this.hasDefault = true;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the column's default value, meaning the value that would be
     * inserted to the database when no value is specified.
     * @return the column's default value String.
     */
    public String getDefaultValue() {
        return this.defaultValue;

    }

    /**
     * Sets if the column can or cannot take null values.
     * @param notNull a boolean that decides if the column isNotNull.
     */
    public void setNotNull(boolean notNull) {
        this.isNotNull = notNull;
    }

    /**
     * Returns true if the column cannot take null values.
     * @return True if the column cannot take null values.
     */
    public boolean isNotNull() {
        return this.isNotNull;
    }

    /**
     * Sets a domain of Strings that the column can take values from.
     * If a value is given that defies this constraint an SQLException will be thrown.
     * This method must be used only when the column is of varchar type.
     * @param domain a String array that defines the domain that this column
     * can take values from.
     */
    public void setDomain(String[] domain) {
        this.isConstrained = true;
        this.hasDomain = true;
        this.domain = domain;
    }

    /**
     * Sets the lowest value the column can take. If a value is given that defies
     * this constraint an SQLException will be thrown.
     * This method must be used only when the column is of numeric type.
     * @param low a String defining the lowest numeric value the column can take.
     */
    public void setLow(String low) {
        this.isConstrained = true;
        this.hasLow = true;
        this.low = low;
    }

    /**
     * Sets the highest value the column can take. If a value is given that defies
     * this constraint an SQLException will be thrown.
     * This method must be used only when the column is of numeric type.
     * @param high a String defining the highest numeric value the column can take.
     */
    public void setHigh(String high) {
        this.isConstrained = true;
        this.hasHigh = true;
        this.high = high;
    }

    /**
     * Sets if the column must have unique values or not.
     * @param isUnique boolean that defines if the column isUnique.
     */
    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    /**
     * Returns true if the column must have unique values.
     * @return True if the column must have unique values.
     */
    public boolean isUnique() {
        return isUnique;
    }

    /**
     * Returns true if the column is a foreign key column.
     * @return True if the column is a foreign key column.
     */
    public boolean isForeignKey() {
        return isForeignKey;
    }

    /**
     * Returns true if the column is automatically generated by the database.
     * @return True if the column is automatically generated by the database.
     */
    public boolean isAutoGenerated() {
        return isAutoGenerated;
    }

    public void setAutoGenerated(boolean autoGenerated){
        this.isAutoGenerated = autoGenerated;
    }

    /**
     * Returns true if the column is constrained by domain or by low-high values.
     * @return True if the column is constrained by domain or by low-high values.
     */
    public boolean isConstrained() {
        return isConstrained;
    }

    /**
     * Returns true if the column has a lowest value constraint.
     * @return True if the column has a lowest value constraint.
     */
    public boolean hasLow() {
        return hasLow;
    }

    /**
     * Returns true if the column has a highest value constraint.
     * @return True if the column has a highest value constraint.
     */
    public boolean hasHigh() {
        return hasHigh;
    }

    /**
     * Returns true if the column has a domain constraint.
     * @return True if the column has a domain constraint.
     */
    public boolean hasDomain() {
        return hasDomain;
    }

    /**
     * Returns the highest value constraint of the column.
     * @return the String that defines the column's highest value.
     */
    public String getHigh() {
        return high;
    }

    /**
     * Returns the lowest value constraint of the column.
     * @return the String that defines the column's lowest value.
     */
    public String getLow() {
        return low;
    }

    /**
     * Returns the column's domain constraint.
     * @return the String array that defines the domain from which the column
     * takes values.
     */
    public String[] getDomain() {
        return domain;
    }

    /**
     * Returns true if the column has a default value.
     * @return True if the column has a default value.
     */
    public boolean hasDefault() {
        return hasDefault;
    }

    /**
     * Returns the Component this column references to, if the column is a
     * foreign key column.
     * @return the Component class this column references to.
     */
    public Class<? extends Component> getReferencesClass() {
        return referencesClass;
    }

    /**
     * Sets the Component this column references to, if the column is a
     * foreign key column.
     * @param referencesClass the Component class this column references to.
     */
    public void setReferencesClass(Class<? extends Component> referencesClass) {
        this.referencesClass = referencesClass;
    }

    /**
     * Returns the class field on which this column was created.
     * @return the Component's Field on which this column was created.
     */
    public Field getField() {
        return field;
    }

    /**
     * Sets the class field on which this column was created.
     * @param field the Component's Field on which this column was created.
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * Returns true if the column has numeric type. Numerics are considered int,
     * big int, small int, decimal, double and real sql types.
     * @return true if the column has numeric type.
     */
    public boolean isTypeNumeric() {
        if (columnType.equals(SQLType.INTEGER)
                || columnType.equals(SQLType.BIGINT)
                || columnType.equals(SQLType.SMALLINT)
                || columnType.equals(SQLType.DECIMAL)
                || columnType.equals(SQLType.DOUBLE)
                || columnType.equals(SQLType.REAL)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TableColumn clone() {
        try {
            return (TableColumn) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        TableColumn other = (TableColumn) obj;
        return other.getFullName().equals(getFullName());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.columnName != null ? this.columnName.hashCode() : 0);
        hash = 19 * hash + (this.masterTable != null ? this.masterTable.hashCode() : 0);
        return hash;
    }

    public void setNumericNull(String numericNull) {
        this.numericNull = numericNull;
    }

    public String getNumericNull() {
        return numericNull;
    }

    
}
