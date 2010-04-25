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
 *
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

    public TableColumn() {
        super();
    }

    public TableColumn(String columnName) {
        setColumnName(columnName);
    }

    public JTable getMasterTable() {
        return masterTable;
    }

    public void setMasterTable(JTable masterTable) {
        this.masterTable = masterTable;
    }

    public String getFullName() {
        if (masterTable == null) {
            return getColumnName();
        }
        return masterTable.getTableName() + "." + getColumnName();
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setColumnType(SQLType columnType) {
        this.columnType = columnType;
    }

    public SQLType getColumnType() {
        return columnType;
    }

    public void setPrimaryKey(boolean isPrimaryKey, boolean isAutoGenerated) {
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoGenerated = isAutoGenerated;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setForeignKey(JTable table, JTableColumn ForeignColumn, OnModification onDelete, OnModification onUpdate) {
        this.isForeignKey = true;
        this.referencesTable = table;
        this.referencesColumn = ForeignColumn;
        this.onDelete = onDelete;
        this.onUpdate = onUpdate;

    }

    public String getReferenceTableName() {
        if (referencesTable==null) return null;
        return referencesTable.getTableName();
    }

    public JTable getReferenceTable() {
        return referencesTable;
    }

    public String getReferenceColumnName() {
        return referencesColumn.getColumnName();
    }

    public JTableColumn getReferenceColumn() {
        return referencesColumn;
    }

    public OnModification getOnDelete() {
        return onDelete;
    }

    public OnModification getOnUpdate() {
        return onUpdate;
    }

    public void setDefaultValue(String defaultValue) {
        this.hasDefault = true;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return this.defaultValue;

    }

    public void setNotNull(boolean notNull) {
        this.isNotNull = notNull;
    }

    public boolean isNotNull() {
        return this.isNotNull;
    }

    public void setDomain(String[] domain) {
        this.isConstrained = true;
        this.hasDomain = true;
        this.domain = domain;
    }

    public void setLow(String low) {
        this.isConstrained = true;
        this.hasLow = true;
        this.low = low;
    }

    public void setHigh(String high) {
        this.isConstrained = true;
        this.hasHigh = true;
        this.high = high;
    }

    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public boolean isAutoGenerated() {
        return isAutoGenerated;
    }

    public boolean isConstrained() {
        return isConstrained;
    }

    public boolean hasLow() {
        return hasLow;
    }

    public boolean hasHigh() {
        return hasHigh;
    }

    public boolean hasDomain() {
        return hasDomain;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String[] getDomain() {
        return domain;
    }

    public boolean hasDefault() {
        return hasDefault;
    }

    public Class<? extends Component> getReferencesClass() {
        return referencesClass;
    }

    public void setReferencesClass(Class<? extends Component> referencesClass) {
        this.referencesClass = referencesClass;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

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
}
