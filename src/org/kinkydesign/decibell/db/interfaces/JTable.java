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

/**
 *
 * A database table containing information about its structure and columns.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JTable {

    /**
     * Add a new column to the table. Columns witout a specified type are not accepted
     * (NullPointerException is thrown).
     * @param column table column to be added in the table.
     */
    void addColumn(JTableColumn column);

    void addRelation(JRelationalTable t);

    /**
     * Get the SQL command for the creation of the table.
     * @return SQL command for table creation.
     */
    String getCreationSQL();

    /**
     * Get the SQL command for the removal of the table from the database.
     * @return SQL command for dropping.
     */
    String getDeletionSQL();

    Set<JTableColumn> getForeignKeyColumns();

    Set<JTableColumn> getPrimaryKeyColumns();

    Set<JRelationalTable> getRelations();

    /**
     * Retrieve the list of columns of the table.
     * @return list of table columns.
     */
    Set<JTableColumn> getTableColumns();

    /**
     * Get the name of the table.
     * @return table name.
     */
    String getTableName();

    /**
     * Get the name of the table.
     * @return table name.
     */
    String getFullTableName();

    /**
     * Get the name of the table.
     * @return table name.
     */
    String getTableSchema();

    /**
     * Remove a column from the table.
     * @param column column to be removed.
     */
    void removeColumn(JTableColumn column);

    /**
     * Declare the list of table columns of the table.
     * @param tableColumns table columns.
     */
    void setTableColumns(Set<JTableColumn> tableColumns);

    /**
     * Set/update the name of the table.
     * @param tableName the name of the table.
     */
    void setTableName(String schema, String tableName);



    Set<Set<JTableColumn>> getForeignColumnsByGroup();



    Set<JTable> getReferencedTables();



    Map<JTableColumn, JTableColumn> referenceRelation();

    Set<JTableColumn> getUniqueColumns();

    /**
     * Whether the table has columns which are foreign keys to itself.
     * @return
     *      <code>true</code> is the table has self-references.
     */
    boolean hasSelfReferences();

    /**
     * Get a list of the foreign columns of the table which reference itself (self-
     * referencing columns) if any. If the table does not have any self references,
     * returns an empty list. The implementation of the returned List is the LinkedList
     * that preserves the order of the columns in the table.
     * @return
     *      List of self-referencing columns of the table.
     */
    List<JTableColumn> getSelfReferences();

}
