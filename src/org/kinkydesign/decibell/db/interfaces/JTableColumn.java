/**
 *  Class : JTableColumn
 * 
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
package org.kinkydesign.decibell.db.interfaces;

import java.lang.reflect.Field;
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.ForeignKey;

/**
 *
 * <p  align="justify" style="width:60%">
 * Interface for a Table Column. A table column from the table point of view consists
 * of its name and data type but from the database and ER point of view holds much more
 * information which involves the whole database structure. This interface provides methods
 * for retrieving this information and modifying properties of the table column.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JTableColumn {

    /**
     * <p  align="justify" style="width:60%">
     * The name of the column. In Derby as well as in most SQL servers, column and
     * table names are not case-sensitive. The column name provided by this method 
     * is the simple name of the column, ommiting the name of the table and the schema
     * or user of the database. You should avoid using dots(.) and special characters
     * to define a column name since these might confuse some SQL servers or even cause
     * exceptions to be thrown. As mentioned in the Derby Reference Manual "A Simple-column-Name
     * is used to represent a column when it cannot be qualified by a <em>table-Name</em>
     * or <em>correlation-Name</em>".
     * </p>
     * @return
     *      The simple name of the column as a String.
     * @see JTableColumn#getFullName() get the full name
     */
    String getColumnName();

    /**
     * <p  align="justify" style="width:60%">
     * Returns the SQL type of the column.
     * </p>
     * @return
     *      Datatype of the column.
     * @see SQLType SQL Types
     * @see JTableColumn#setColumnType(org.kinkydesign.decibell.collections.SQLType) setColumnType
     */
    SQLType getColumnType();

    /**
     *
     * <p  align="justify" style="width:60%">
     * Returns the default value assigned to the column, or <code>null</code> if
     * not any.
     * </p>
     * @return
     *      The default value of the column as a String.
     * @see JTableColumn#setDefaultValue(java.lang.String) setDefaultValue
     */
    String getDefaultValue();

    /**
     *
     * <p  align="justify" style="width:60%">
     * One may impose a constraint on a table column (upon table creation) requiring
     * that its values should vary in a finite set. This being the case, this method
     * returns this finite domain of the column or <code>null</code> otherwise.
     * </p>
     * @return
     *      The domain of the table column (if any) or <code>null</code>.
     */
    String[] getDomain();

    /**
     *
     * <p  align="justify" style="width:60%">
     * Retrieve the field (if any) from which the current table column occured. Being an
     * object-database mapper, DeciBell keeps track of the connection between tables/columns
     * and objects/fields. This method allows to get from the database world to the java world!
     * </p>
     * @return
     *      The Field that generated the Table Column.
     */
    Field getField();

    /**
     *
     * <p  align="justify" style="width:60%">
     * Returns the full name of a column which consists of the full table name and the
     * simple column name according to the general syntax <tt>{TABLE NAME}.{COLUMN NAME}</tt>. For
     * example in Derby, if the user <tt>JOHN</tt> is connected to the database and
     * owns the table <tt>ORDERS</tt> which has the column <code>ORD_NO</code>, then
     * the full name of the column would be <tt>JOHN.ORDERS.ORD_NO</tt>.
     * </p>
     * @return
     *      The full name of a column
     * @see JTableColumn#getColumnName() simple name
     */
    String getFullName();

    /**
     *
     * If there is an active numeric constrain on that column, get the higher allowed
     * value imposed by the constraint, otherwise returns <code>null</code>.
     * @return
     *      Maximum allowed value for the column, or <code>null</code> if not any.
     * @see JTableColumn#getLow() getLow()
     */
    String getHigh();

    /**
     *
     * If there is an active numeric constrain on that column, get the lower allowed
     * value imposed by the constraint, otherwise returns <code>null</code>.
     * @return
     *      Minimum allowed value for the column, or <code>null</code> if not any.
     * @see JTableColumn#getHigh() getHigh()
     */
    String getLow();

    /**
     *
     * <p align="justify" style="width:60%">
     * Retrieve the table that owns this column which is called <tt>master table</tt>.
     * </p>
     * @return
     *      The master table.
     */
    JTable getMasterTable();

    /**
     *
     * <p align="justify" style="width:60%">
     * Retrieve the action which is automaticaly performed when a delete operation
     * is applied on the table. This kind of constraint may be imposed only on
     * foreign key columns.
     * </p>
     * @return
     *      Action after deletion of an entry.
     * @see ForeignKey#onDelete() @ForeignKey.onDelete()
     * @see JTableColumn#setForeignKey(org.kinkydesign.decibell.db.interfaces.JTable,
     * org.kinkydesign.decibell.db.interfaces.JTableColumn,
     * org.kinkydesign.decibell.collections.OnModification,
     * org.kinkydesign.decibell.collections.OnModification) configure Foreign key
     */
    OnModification getOnDelete();

    /**
     *
     * <p align="justify" style="width:60%">
     * Retrieve the action which is automaticaly performed when an update operation
     * is applied on the table. This kind of constraint may be imposed only on
     * foreign key columns.
     * </p>
     * @return
     *      Action after deletion of an entry.
     * @see ForeignKey#onUpdate()  @ForeignKey.onUpdate
     * @see JTableColumn#setForeignKey(org.kinkydesign.decibell.db.interfaces.JTable,
     * org.kinkydesign.decibell.db.interfaces.JTableColumn,
     * org.kinkydesign.decibell.collections.OnModification,
     * org.kinkydesign.decibell.collections.OnModification) configure Foreign key
     */
    OnModification getOnUpdate();

    JTableColumn getReferenceColumn();

    String getReferenceColumnName();

    /**
     *
     * <p align="justify" style="width:60%">
     * Retrieve the table which is referenced by a foreign key on the master table,
     * i.e. the slave table with respect to this master column (if the column is a
     * foreign key). If the column does not point to some other entity, returns <code>
     * null</code>. Note that this information is specified by the method {@link
     * JTableColumn#setForeignKey(org.kinkydesign.decibell.db.interfaces.JTable,
     * org.kinkydesign.decibell.db.interfaces.JTableColumn,
     * org.kinkydesign.decibell.collections.OnModification,
     * org.kinkydesign.decibell.collections.OnModification) setForeignKey(*)}.
     * </p>
     * @return
     *      The slave table.
     */
    JTable getReferenceTable();

    /**
     *
     * <p align="justify" style="width:60%">
     * The name of the slave table if any (if the column is a foreign key to some
     * other entity) or </code>null</code> otherwise. Note that the same information
     * can be retrieved using a combination of the methods {@link JTableColumn#getReferenceTable() } and
     * {@link JTable#getTableName() } but this is a more convenient way to do that.
     * </p>
     * @return
     *      The name of the slave table (if any) or </code>null</code> if the column
     *      is not a foreign key.
     */
    String getReferenceTableName();

    /**
     *
     * <p align="justify" style="width:60%">
     * The class which is referenced by this column (if any) or <code>null</code> if
     * the column is not a foreign key. Note that all tables in Decibell except for the
     * relational ones are generated by a class (subclass of {@link Component } ) which
     * is attached on a {@link DeciBell } object.
     * </p>
     * @return
     *      Class beign referenced by this column (if it is a foreign key column)
     *      or <code>null</code> otherwise.
     */
    Class<? extends Component> getReferencesClass();

    /**
     * 
     * <p align="justify" style="width:60%">
     * Whether the table column has some default value which should be registered
     * if one attempts to register a <code>null</code> value instead. If a column is
     * not nullable, it would be good practise to specify some default value to 
     * compensate for the nulls.
     * </p>
     * @return
     *      <code>true</code> if there is some specified default value for the table 
     *      column and <code>false</code> otherwise.
     */
    boolean hasDefault();

    /**
     * <p align="justify" style="width:60%">
     * Whether some finite domain constraint is imposed on the column
     * </p>
     * @return
     *      <code>true</code> if there is a finite domain constraint imposed
     *      and <code>false</code> otherwise.
     */
    boolean hasDomain();

    boolean hasHigh();

    boolean hasLow();

    /**
     *
     * <p align="justify" style="width:60%">
     * Whether this column is a primary key and autogenerated as identity.
     * </p>
     * @return
     *      <code>true</code> if the column is a PK autogenerated as id and
     *      <code>false</code> otherwise.
     */
    boolean isAutoGenerated();

    /**
     *
     * <p align="justify" style="width:60%">
     * Whether there are constraints imposed on the column.
     * </p>
     * @return
     *      <code>true</code> if there are imposed constraints on the column and
     *      <code>false</code> otherwise.
     */
    boolean isConstrained();

    /**
     *
     * <p align="justify" style="width:60%">
     * Whether the column is a foreign key to some other table
     * </p>
     * @return
     *      <code>true</code> if the column has a foreign key constraint and
     *      <code>false</code> otherwise.
     */
    boolean isForeignKey();

    /**
     * <p align="justify" style="width:60%">
     * Whether this table column is not nullable.
     * </p>
     * @return
     *      <code>true</code> if the column is not-nullable (<code>null</code> is
     *      not an allowed value for the entries in this column) and <code>false
     *      </code> otherwise.
     */
    boolean isNotNull();

    /**
     *
     * <p align="justify" style="width:60%">
     * Whether this table column is a primary key column. Primary key columns are
     * used to uniquely specify an entry in the table. A table may have more than
     * one primary key columns (multiple primary key). This method returns <code>
     * true</code> if the column is itself the single primary key of the table or
     * if it participates in some bundle of primary keys.
     * </p>
     * @return
     *      <code>true</code> if the column has a primary key constraint or participates
     *      in a multiple primary key and <code>false</code> otherwise.
     */
    boolean isPrimaryKey();

    /**
     *
     * <p align="justify" style="width:60%">
     * Whether the SQLType (datatype) of this column, as provided by the method
     * {@link JTableColumn#getColumnType() getColumnType}, is numeric, i.e. one of
     * {@link SQLType#BIGINT BIGINT}, {@link SQLType#DECIMAL DECIMAL}, {@link SQLType#DOUBLE DOUBLE},
     * {@link SQLType#INTEGER INTEGER}, {@link SQLType#REAL REAL} or
     * {@link SQLType#SMALLINT SMALLINT}.
     * </p>
     * @return
     *      <code>true</code> if the datatype of the column is numeric and
     *      <code>false</code> otherwise.
     */
    boolean isTypeNumeric();

    /**
     * 
     * <p align="justify" style="width:60%">
     * Whether the column has a <code>unique</code> constraint.
     * </p>
     * @return
     *      <code>true</code> if the column has a <code>unique</code> constraint
     *      and <code>false</code> otherwise.
     */
    boolean isUnique();

    /**
     *
     * <p align="justify" style="width:60%">
     * Set the name of the column; provide the simple name. Implementations of this
     * interface may also provide a constructor with the column name as an input argument.
     * </p>
     * @param columnName
     *      The (simple) name of the column.
     */
    void setColumnName(String columnName);

    /**
     * 
     * <p align="justify" style="width:60%">
     * Set the SQL datatype of the column using the enumeration {@link SQLType }.
     * Implementations of this interface may also provide a constructor with the SQL
     * type as an input argument.
     * </p>
     * @param columnType
     *      The SQL type of the column.
     */
    void setColumnType(SQLType columnType);

    /**
     * <p align="justify" style="width:60%">
     * Set the default value of the column, that is the value to be assigned to
     * the entries of this column if one attempts to register an entry in the
     * corresponding table ommiting to specify the value for this column.
     * </p>
     * @param defaultValue
     *      The default value of the column.
     */
    void setDefaultValue(String defaultValue);

    /**
     * 
     * <p align="justify" style="width:60%">
     * Specify a finite domain constraint for the table column. One must not
     * stimultaneously provide a finite domain and a higher and/or lower value
     * using the methods {@link JTableColumn#setHigh(java.lang.String) setHigh}
     * and {@link JTableColumn#setLow(java.lang.String) setLow}.
     * </p>
     * @param domain
     *      String array with the allowed values for the table column.
     */
    void setDomain(String[] domain);

    /**
     *
     * <p align="justify" style="width:60%">
     * Set the java field to which this column refers to (if any). If the column
     * is held by a relational table, the field might be <code>null</code>.
     * </p>
     * @param field
     *      The java Field corresponding to the table column.
     */
    void setField(Field field);

    /**
     *
     * <p  align="justify" style="width:60%">
     * Set the table column to be a foreign key to another table/entity. The <em>referencing
     * column</em> (this) is also known as <em>master column</em> and the table to
     * which is belongs is often called the <em>master table</em> which the referenced
     * table is called <em>slave</em> or <em>remote</em>. The column of the <em>slave</em>
     * table which is referenced by the <em>master column</em> has also to be provided
     * in order to qualify a well defined reference.
     * </p>
     * <p align="justify" style="width:60%">
     * Additionally you can specify the actions to be automatically performed on update
     * and on delete.
     * </p>
     * @param table
     *      The slave/referenced table which is referenced by this (master) column
     * @param ForeignColumn
     *      The slave/referenced column in the slave table.
     * @param onDelete
     *      Action to be performed upon the execution of a <code>DELETE</code>
     *      SQL statement.
     * @param onUpdate
     *      Action to be performed upon the execution of a <code>UPDATE</code>
     *      SQL statement.
     * @see OnModification
     * @see ForeignKey Foreign Key Constraint
     */
    void setForeignKey(JTable table,
            JTableColumn ForeignColumn,
            OnModification onDelete, OnModification onUpdate);

    /**
     *
     * <p align="justify" style="width:60%">
     * Provide the higher acceptable value for the table column.  One must not
     * stimultaneously provide a finite domain and a higher value
     * using the method <code>setHigh</code>
     * and {@link JTableColumn#setDomain(java.lang.String[]) setDomain}. Use one of these
     * methods exclusively. <code>setHigh</code> can be combined with {@link
     * JTableColumn#setLow(java.lang.String) setLow}.
     * </p>
     * @param high
     *      The higher acceptable value for the table column as a String.
     */
    void setHigh(String high);

    /**
     *
     * <p align="justify" style="width:60%">
     * Provide the lower/minimum acceptable value for the table column.  One must not
     * stimultaneously provide a finite domain and a lower value
     * using the method <code>setLow</code>
     * and {@link JTableColumn#setDomain(java.lang.String[]) setDomain}. Use one of these
     * methods exclusively. <code>setLow</code> can be combined with {@link
     * JTableColumn#setHigh(java.lang.String) setHigh}.
     * </p>
     * @param low
     *      The higher acceptable value for the table column as a String.
     */
    void setLow(String low);

    /**
     *
     * <p align="justify" style="width:60%">
     * Set the master table of this column, i.e. the table that holds this column.
     * </p>
     * @param masterTable
     *      The table that owns this column
     */
    void setMasterTable(JTable masterTable);

    /**
     *
     * <p align="justify" style="width:60%">
     * Setting a table column to not-null (such a column is called not-nullable),
     * an SQL Exception will be thrown if one tries to register a <code>null</code>.
     * </p>
     * @param notNull
     *      <code>true</code> if the column is not-nullable.
     */
    void setNotNull(boolean notNull);

    /**
     *
     * <p align="justify" style="width:60%">
     * Declare that this column is a primary key column of the table. Note that
     * multiple columns are allowed to be primary keys of the table in the sense that
     * they uniquely identify its entries as a tuple.
     * </p>
     * @param isPrimaryKey
     *      Whether the column is a primary key column (<code>true</code> if yes).
     * @param isAutoGenerated
     *      Whether the primary key column is autogenerated as identity, i.e. some
     *      auto-incrementing integer value.
     * @see JTableColumn#setUnique(boolean) unique entry
     */
    void setPrimaryKey(boolean isPrimaryKey, boolean isAutoGenerated);

    /**
     *
     *
     * @param referencesClass
     */
    void setReferencesClass(Class<? extends Component> referencesClass);

    /**
     *
     * <p align="justify" style="width:60%">
     * Define the table column as </code>unique</code>, in the sense that no multiples
     * of the same value can be found in its entries hence the column uniquely identifies
     * its rows.
     * </p>
     * @param isUnique
     *      Whether the table column is </code>unique</code> (<code>true</code> if yes).
     * @see JTableColumn#setPrimaryKey(boolean, boolean) primary key
     */
    void setUnique(boolean isUnique);

    /**
     * <p align="justify" style="width:60%">
     * Sets the NumericNull value for this column.
     * </p>
     * @param numericNull a NumericNull String value
     */
    void setNumericNull(String numericNull);

    /**
     * <p align="justify" style="width:60%">
     * Returns this column's NumericNull value.
     * </p>
     * @return this column's NumericNull value.
     */
    String getNumericNull();
}
