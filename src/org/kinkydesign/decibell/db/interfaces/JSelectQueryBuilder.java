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

import java.util.ArrayList;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.table.Table;
import org.kinkydesign.decibell.db.table.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JSelectQueryBuilder {

    /**
     *
     * Attach another table to the Query Builder which is used as a secondary table.
     *
     * <p>
     * Attach a table in the query builder which is used as a secondary table, i.e. 
     * participates in the inner-join part of the query and can also be used to 
     * perform complex search operations. Let us give a complete example of use.
     * </p>
     *
     * <p>
     * Let <code>User</code> be the base table (defined in the constructor) with
     * three columns <code>UserName</code>, <code>PassWord</code> and <code>group</code>
     * (foreign key referencing the column groupName in userGroups). Let <code>User_group</code>
     * be some other table with columns <code>GroupName</code> (primary key)
     * and <code>Level</code>. Then let's see how could one exploit DeciBell&copy; to
     * construct an SQL query for a complex select operation.
     * </p>
     *
     * <p><pre>
     * <code>TableColumn userNameCol = new TableColumn("userName");
     * userNameCol.setColumnType(SQLType.VARCHAR);
     *
     * TableColumn passwordCol = new TableColumn("PassWord");
     * passwordCol.setColumnType(SQLType.LONG_VARCHAR);
     *
     * TableColumn groupCol = new TableColumn("group");
     * groupCol.setColumnType(SQLType.LONG_VARCHAR);
     *
     * TableColumn groupNameCol = new TableColumn("GroupName");
     * groupNameCol.setColumnType(SQLType.VARCHAR);
     *
     * TableColumn levelCol = new TableColumn("Level");
     * levelCol.setColumnType(SQLType.VARCHAR);
     *
     * Table users = new Table("USERS");
     * users.addColumn(userNameCol);
     * users.addColumn(passwordCol);
     * Table userGroups = new Table("USER_GROUPS");
     * userGroups.addColumn(groupNameCol);
     * userGroups.addColumn(levelCol);
     *
     * JSelectQueryBuilder builder = new SelectQueryBuilder(users);
     * builder.attach(userGroups, groupCol, groupNameCol);
     *
     * System.out.println(builder.selectQuery());
     * </code>
     * </pre>
     * </p>
     *
     * The above example prints the following output to the console:<br/>
     * <pre>
     * <code>SELECT USERS.* FROM USERS
     *   INNER JOIN USER_GROUPS ON USER_GROUPS.GroupName  = USERS.group
     * </code></pre>
     * which is exactly what one was expecting for.
     * 
     * @param otherTable
     *      Some table to be used a secondary table for the SQL query.
     *
     * @param localColumn
     *      The column of the abovementioned table to be used as a reference for the
     *      inner join. This is a column of the base table as defined in the constructor of
     *      the query builder.
     *
     * @param remoteColumn
     *      Column of the secondary table, being referenced by the <code>localColumn<code>
     *      of the base table.
     *
     * @throws IllegalArgumentException
     *          In case the table column defined is not a column of the table.
     *
     * @see JSelectQueryBuilder#selectQuery() simple <code>SELECT</code> Query
     * @see JSelectQueryBuilder#selectQuery(java.util.ArrayList) <code>SELECT</code> query with search options
     *
     */
    void attach(Table otherTable, TableColumn localColumn, TableColumn remoteColumn) throws IllegalArgumentException;


    /**
     *
     * SQL query creation without support for search operations.
     * <p>
     * Create an {@link SQLQuery } with respect to the specified options for the
     * Query Builder. This is a <code>SELECT</code>-type SQL query with or without
     * an inner join with some other table. The queries constructed by this method
     * do not include the keyword <code>WHERE</code>, i.e. there is no <em>search</em>
     * functionallity supported. This means that these queries return all the data
     * contained in the corresponding table(s).
     * </p>
     * @return
     *      SQL Query for selection with no search functionallity.
     * @see JSelectQueryBuilder#selectQuery(java.util.ArrayList) Select with search support
     */
    SQLQuery selectQuery();

    /**
     *
     * @param propositions
     * @return
     */
    SQLQuery selectQuery(ArrayList<Proposition> propositions);

    /**
     *
     * @param propositions
     * @param logicalOperator
     * @return
     *      The SQL query produced by the Query Builder.
     */
    SQLQuery selectQuery(ArrayList<Proposition> propositions, LogicalOperator logicalOperator);

    /**
     *
     * @param propositions
     * @param logicalOperators
     * @return
     *      The <code>SELECT</code> query produced by the Query Builder.
     * @throws IllegalArgumentException
     */
    SQLQuery selectQuery(ArrayList<Proposition> propositions, ArrayList<LogicalOperator> logicalOperators) throws IllegalArgumentException;

    /**
     * Create a search query the easy way.
     * @return
     *      search query including only equalities in the propositions.
     */
    SQLQuery easySearchQueryEquality();

}
