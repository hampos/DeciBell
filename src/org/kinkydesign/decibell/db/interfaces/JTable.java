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
package org.kinkydesign.decibell.db.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kinkydesign.decibell.collections.SQLType;

/**
 * <p  align="justify" style="width:60%">
 * A database table containing information about its structure and columns.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JTable {

    /**
     * <p  align="justify" style="width:60%">
     * Add a new column to the table. Columns witout a specified type are not accepted
     * (NullPointerException is thrown).
     * </p>
     * @param column table column to be added in the table.
     */
    void addColumn(JTableColumn column);

    /**
     * <p  align="justify" style="width:60%">
     * Add a relational interconnection between two tables defining a
     * reltational table which links this table with some other (or this table
     * itself). You may add many such relational tables invoking the method successively.
     * </p>
     * @param t
     *      A relational table.
     * @see JRelationalTable
     */
    void addRelation(JRelationalTable t);

    /**
     * <p  align="justify" style="width:60%">
     * Get the SQL command for the creation of the table.
     * </p>
     * @return SQL command for table creation.
     */
    String getCreationSQL();

    /**
     * <p  align="justify" style="width:60%">
     * Get the SQL command for the removal of the table from the database.
     * </p>
     * @return SQL command for dropping.
     */
    String getDeletionSQL();

    /**
     * <p  align="justify" style="width:60%">
     * Get the collection of all foreign key columns in this table.
     * </p>
     * @return
     *      <code>Set</code> of all foreign key columns.
     */
    Set<JTableColumn> getForeignKeyColumns();

    /**
     * <p  align="justify" style="width:60%">
     * Get the set of all primary key columns of the table.
     * </p>
     * @return
     *      <code>Set</code> of all primary key columns.
     */
    Set<JTableColumn> getPrimaryKeyColumns();

    /**
     * <p  align="justify" style="width:60%">
     * The set of all relational tables having this table as their master references.
     * Every such relational table corresponds to a many-to-many or one-to-many relation
     * between this table and some other (or even itself). Such MtM or OtM relations
     * are generated by a collection of Components in the class that triggered the
     * generation of this table.
     * </p>
     * @return
     */
    Set<JRelationalTable> getRelations();

    /**
     * <p  align="justify" style="width:60%">
     * Retrieve the list of columns of the table including all primary key and
     * foreign key ones.
     * </p>
     * @return
     *      List of table columns.
     */
    Set<JTableColumn> getTableColumns();

    /**
     * <p  align="justify" style="width:60%">
     * Get the name of the table. This method will not return the name of the schema
     * withing the name of the table, for example if you have a table called <code>MY_TABLE</code>
     * in schema <code>JOHN</code>, the complete name of the table is <code>JOHN.MY_TABLE</code>.
     * This method will just return <code>MY_TABLE</code>. If you need the full table
     * name consider using {@link JTable#getFullTableName() getFullTableName()}.
     * </p>
     * @return 
     *      simple table name.
     * @see JTable#getTableSchema() get schema name
     */
    String getTableName();

    /**
     * <p  align="justify" style="width:60%">
     * Get the full name of the table which is the simple name of the table
     * augmented by the name of the schema following the general standard
     * <code>SCHEMA_NAME.TABLE_NAME</code>.
     * </p>
     * @return
     *      full table name.
     * @see JTable#getTableName() get simple table name
     */
    String getFullTableName();

    /**
     * <p  align="justify" style="width:60%">
     * Get the name of the schema in which the table is found.
     * </p>
     * @return
     *      schema name.
     */
    String getTableSchema();

    /**
     * <p  align="justify" style="width:60%">
     * Remove a column from the table.
     * </p>
     * @param column 
     *      A column to be removed.
     */
    void removeColumn(JTableColumn column);

    /**
     * <p  align="justify" style="width:60%">
     * Declare the list of table columns of the table.
     * </p>
     * @param tableColumns table columns.
     */
    void setTableColumns(Set<JTableColumn> tableColumns);

    /**
     * <p  align="justify" style="width:60%">
     * Set/update the name of the table.
     * </p>
     * @param tableName 
     *      The name of the table.
     * @param schema
     *      The name of the schema in which the table is found.
     */
    void setTableName(String schema, String tableName);

    /**
     * <p  align="justify" style="width:60%">
     * Get the foreign key columns of the table grouped in sets of columns that
     * point to the same table and refer to the same property. This property in
     * the Java code corresponds to a <code>field</code>. This method groups together
     * all foreign key columns that should be handled as a group and their cartesian
     * product forms the key under consideration.
     * </p>
     * @return
     *      Set of grouped foreign key columns.
     */
    Set<Set<JTableColumn>> getForeignColumnsByGroup();

    /**
     * <p  align="justify" style="width:60%">
     * The set of all tables for which there exists a column in this table that
     * references a column therein.
     * </p>
     * @return
     *      Set of referenced tables.
     */
    Set<JTable> getReferencedTables();

    /**
     * <p  align="justify" style="width:60%">
     * The complete collection of column-to-column master-slace relations. Contains
     * all pairs of columns such that the first (master) references the second
     * (slave).
     * </p>
     * @return
     *      Collection of reference relations as a <code>Map</code>.
     */
    Map<JTableColumn, JTableColumn> referenceRelation();

    /**
     * <p  align="justify" style="width:60%">
     * Return the set of all columns of the table tagged as <code>unique</code>.
     * </p>
     * @return
     *      Set of unique columns.
     */
    Set<JTableColumn> getUniqueColumns();

    /**
     * <p  align="justify" style="width:60%">
     * Whether the table has columns which are foreign keys to itself.
     * </p>
     * @return
     *      <code>true</code> is the table has self-references.
     */
    @Deprecated
    boolean hasSelfReferences();

    /**
     * <p  align="justify" style="width:60%">
     * Get a list of the foreign columns of the table which reference itself (self-
     * referencing columns) if any. If the table does not have any self references,
     * returns an empty list. The suggested implementation of the returned <code>List</code>
     * is the <code>LinkedList</code> that preserves the order of the columns in the table
     * and provides other facilities as well. The method returns an empty list if
     * no self-referencing columns are found.
     * </p>
     * @return
     *      List of self-referencing columns of the table.
     */
    List<JTableColumn> getSelfReferences();

    /**
     * <p  align="justify" style="width:60%">
     * Autogenerated column of the table (if any) or <code>null</code> if the table
     * does not posses any auto-generated columns.
     * </p>
     * @return
     *      The autogenerated column of the table (if any) or <code>null</code>
     *      otherwise.
     */
    JTableColumn getAutogeneratedColumn();

    /**
     * <p  align="justify" style="width:60%">
     * Specify or update the autogenerated column of this table (if any). Note that
     * every table is allowed to have at most one auto-generated column and that
     * the only datatypes allowed for the underlying column are {@link SQLType#INTEGER Integer}
     * and {@link SQLType#BIGINT Big Integer}.
     * </p>
     * @param autoGeneratedColumn
     *
     */
    void setAutoGeneratedColumn(JTableColumn autoGeneratedColumn);

    /**
     * <p  align="justify" style="width:60%">
     * Whether the table contains references (foreign keys) to itself (such tables
     * are called self-referencing).
     * </p>
     * @return
     *      <code>true</code> if the table is self-referencing,
     *      <code>false</code> otherwise.
     * @see JTable#getForeignKeyColumns() get all FKs
     *  @see JTable#getForeignColumnsByGroup()  get grouped FKs
     */
    boolean isSelfReferencing();
}
