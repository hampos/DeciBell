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
package org.kinkydesign.decibell.db.derby;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.kinkydesign.decibell.annotations.Constraint;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.TableName;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.TablesGenerator;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 * <p  align="justify" style="width:60%">
 * DerbyTablesGenerator initializes a new database by inspecting the structure of the
 * attached {@link Component components} to a {@link DeciBell DeciBell} object, parses
 * the DeciBell&copy; annotations therein and produces the corresponding SQL structure
 * which also stores in memory (in a {@link ComponentRegistry Component Registry}).
 * Constructs new {@link JTable tables} to which assigns proper {@link JTableColumn table
 * columns} and exploits the method {@link JTable#getCreationSQL() JTable.getCreationSQL()}.
 * The tables generator is initialized with a {@link DbConnector Database Connector} and a
 * set of attached Components and then the method {@link DerbyTablesGenerator#construct() construct}
 * is invoked and the tables are constructed in the database.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DerbyTablesGenerator extends TablesGenerator {

    /**
     * <p  align="justify" style="width:60%">
     * It is possible that a column in some table, is a foreign key to the
     * table itself. In that case, some problems appear during the table creation
     * because the table under creation expects the completion of itself. So, we
     * store these columns in that map (Column to the table holding the column)
     * to register them at the end of the table creation.
     * </p>
     */
    private List<JTableColumn> selfReferencingCols =
            new LinkedList<JTableColumn>();

    /**
     * <p  align="justify" style="width:60%">
     * Initialize a new table generator for the Derby JDBC server.
     * </p>
     * @param db
     *      The DeciBell object that the Tables Generator will use to create the tables. Identifies
     *      the database in which the tables are created.
     * @param components
     *      Set of classes that extends {@link Component } with respect to which
     *      the table creation takes place.
     */
    public DerbyTablesGenerator(DeciBell db, Set<Class<? extends Component>> components) {
        super(db, components);
    }

    private void initSchema() {
        JTable initTable = new DerbyTable();
        initTable.setTableName(db.getDbConnector().getUser(), "DECIBELL_INIT_TAB");
        JTableColumn initColumn = new TableColumn("AA");
        initColumn.setColumnType(SQLType.SMALLINT);
        initTable.addColumn(initColumn);
        String initCreationSQL = initTable.getCreationSQL();
        db.getDbConnector().execute(initCreationSQL);


    }

    public void construct() {
        /*
         * First, create an 'initialization' table, just to
         * initialize the schema and avoid some exceptions...
         */
        initSchema();

        for (Class<? extends Component> c : components) {
            tableCreation(c);
        }
        relTableCreation();

        String SQL;
        Iterator<JTable> it = ComponentRegistry.getRegistry(db.getDbConnector()).values().iterator();
        while (it.hasNext()) {
            SQL = it.next().getCreationSQL();
            db.getDbConnector().execute(SQL);
        }

        Iterator<JRelationalTable> relit = registry.getRelationTables().iterator();
        while (relit.hasNext()) {
            SQL = relit.next().getCreationSQL();
            db.getDbConnector().execute(SQL);
        }

    }

    /*
     * If the field is simply annotated as @Entry, assign to the column
     * all Entry-specific characteristics of the field, like its default
     * value and uniqueness attribute.
     */
    private void handleEntry(JTableColumn column, Annotation ann) {
        Entry entry = (Entry) ann;
        column.setUnique(entry.unique());
        column.setNotNull(entry.notNull());
        if (!entry.defaultValue().isEmpty()) {
            column.setDefaultValue(entry.defaultValue());
        }
        column.setAutoGenerated(entry.autoGenerated());
    }

    private void handleConstraint(JTableColumn column, Annotation ann) {
        Constraint constraint = (Constraint) ann;
        boolean hasHighLowConstraint = false;
        if (!constraint.low().isEmpty()) {
            column.setLow(constraint.low());
            hasHighLowConstraint = true;
        }
        if (!constraint.high().isEmpty()) {
            column.setHigh(constraint.high());
            hasHighLowConstraint = true;
        }
        boolean dom = false;
        for (String s : constraint.domain()) {
            if (!s.isEmpty()) {
                dom = true;
            }
        }
        if (dom) {
            if (hasHighLowConstraint) {
                throw new RuntimeException("You cannot provide bound constraints and "
                        + "finite domain constraints at the same time!");
            }
            column.setDomain(constraint.domain());
        }
    }

    private void checkCollectionConsistency(Field field) {
        Type type = field.getGenericType();
        if (!(type instanceof ParameterizedType)) {
            throw new ClassCastException("Bad ForeignKey specified - Class:" + field.getDeclaringClass().getName()
                    + " Field:" + field.getName() + " contains unknown types");
        } else {
            ParameterizedType pt = (ParameterizedType) type;
            for (Type arg : pt.getActualTypeArguments()) {
                Class carg = (Class) arg;
                if (!isComponent(carg)) {
                    throw new ClassCastException("Bad ForeignKey specified - Class:" + field.getDeclaringClass().getName()
                            + " Field:" + field.getName() + " contains non-Component types");
                }
            }
        }
        relations.add(field);
    }

    private void handleForeignKey(Field masterField, JTableColumn masterColumn, JTableColumn remoteColumn, ForeignKey fk, JTable table) {
        JTableColumn foreignColumn = new TableColumn();
        if (masterColumn.hasDefault()) {
            foreignColumn.setDefaultValue(masterColumn.getDefaultValue());
        }
        if (masterColumn.isConstrained()) {
            if (masterColumn.hasHigh()) {
                foreignColumn.setHigh(masterColumn.getHigh());
            }
            if (masterColumn.hasLow()) {
                foreignColumn.setLow(masterColumn.getLow());
            }
            if (masterColumn.hasDomain()) {
                foreignColumn.setDomain(masterColumn.getDomain());
            }
        }
        foreignColumn.setNotNull(masterColumn.isNotNull());
        foreignColumn.setPrimaryKey(masterColumn.isPrimaryKey());
        foreignColumn.setUnique(masterColumn.isUnique());
        foreignColumn.setColumnName(masterField.getName() + UNDERSCORE + remoteColumn.getColumnName());
        foreignColumn.setColumnType(remoteColumn.getColumnType());
        foreignColumn.setForeignKey((Table) registry.get((Class<? extends Component>) masterField.getType()),
                remoteColumn, fk.onDelete(), fk.onUpdate());
        foreignColumn.setReferencesClass((Class<? extends Component>) masterField.getType());
        foreignColumn.setField(masterField);
        table.addColumn(foreignColumn);
    }

    private void tableCreation(Class<? extends Component> c) {
        Table table = new DerbyTable();
        // if c already in the registry, it is created - return
        if (registry.containsClass(c)) {
            return;
        }
        /*
         * If the submitted class is not a direct subclass of Component, first
         * create the table of its superclass except if its superclass is already
         * registered in the registry.
         */
        if (c.getSuperclass() != Component.class
                && !registry.containsClass((Class<? extends Component>) c.getSuperclass())) {
            tableCreation((Class<? extends Component>) c.getSuperclass());
        }

        // set the name to the generated table
        Annotation declaredTableName = c.getAnnotation(TableName.class);
        if (declaredTableName == null) {
            table.setTableName(db.getUser(), c.getName().replace(DOT, UNDERSCORE));
        } else {
            TableName tableName = (TableName) declaredTableName;
            table.setTableName(db.getUser(), tableName.value());
        }

        for (Field field : c.getDeclaredFields()) {// Iterate over every field in the submitted class.
            boolean flag = false;

            JTableColumn column = new TableColumn();  // Construct the column that corresponds to the current field
            column.setColumnName(field.getName());
            column.setColumnType(TypeMap.getSQLType(field.getType()));

            Annotation ann = null;

            if ((ann = field.getAnnotation(PrimaryKey.class)) != null) {
                column.setPrimaryKey(true);
                flag = true;
            }

            if ((ann = field.getAnnotation(Entry.class)) != null) {
                handleEntry(column, ann);
                flag = true;
            }

            if ((ann = field.getAnnotation(Constraint.class)) != null) {
                handleConstraint(column, ann);
            }

            if ((ann = field.getAnnotation(ForeignKey.class)) != null) { // Case where the field is annotated as a foreign key.
                ForeignKey fk = (ForeignKey) ann;
                if (isCollection(field.getType())) { // The foreign key is a collection, e.g. an ArrayList, List or aSet like HashSet.
                    checkCollectionConsistency(field);
                    flag = false;
                } else {// The foreign key points to some other entity which must be a component and is not a collection of such.
                    if (!isComponent(field.getType())) { // ... the type of the foreign key has to be of type 'Component'
                        throw new ClassCastException("Bad ForeignKey specified - Class:" + field.getDeclaringClass().getName()
                                + " Field:" + field.getName() + " is not a Component");
                    }

                    if (field.getType().equals(c)) {// If the foreign key points to the same table (SR)
                        Iterator<JTableColumn> primaryKeyColumns = table.getPrimaryKeyColumns().iterator();
                        JTableColumn correspondingPK = null;
                        while (primaryKeyColumns.hasNext()) {
                            correspondingPK = primaryKeyColumns.next();
                            column = new TableColumn();
                            column.setMasterTable(table);
                            column.setForeignKey(table, correspondingPK, fk.onDelete(), fk.onUpdate());
                            column.setField(field);
                            column.setReferencesClass(c);
                            column.setColumnName(field.getName() + UNDERSCORE + correspondingPK.getField().getName());
                            selfReferencingCols.add(column);
                        }
                    } else {
                        if (!registry.containsClass((Class<? extends Component>) field.getType())) {
                            tableCreation((Class<? extends Component>) field.getType());
                        }
                        for (JTableColumn col : registry.get((Class<? extends Component>) field.getType()).getPrimaryKeyColumns()) {
                            handleForeignKey(field, column, col, fk, table);
                            flag = false;
                        }
                    }
                }
            }// end of FK case

            if ((ann = field.getAnnotation(NumericNull.class)) != null) {
                NumericNull numNull = (NumericNull) ann;
                column.setNumericNull(numNull.value());
            }

            if (flag) {
                column.setField(field);
                table.addColumn(column);
            }
        }// end of iteration over all fields

        /**
         * The table may not have a primary key column it self if it is produced
         * by a class which subclasses the class Component indirectly, hence the
         * primary key of this table is held by some super-table.
         */
        if (table.getPrimaryKeyColumns().isEmpty()) {
            if (c.getSuperclass() != Component.class) {
                Table superTable = (Table) registry.get((Class<? extends Component>) c.getSuperclass());
                for (JTableColumn col : superTable.getPrimaryKeyColumns()) {
                    TableColumn column = ((TableColumn) col).clone();
                    column.setForeignKey(superTable, col, OnModification.CASCADE, OnModification.NO_ACTION);
                    column.setAutoGenerated(false);
                    column.setReferencesClass((Class<? extends Component>) c.getSuperclass());
                    table.addColumn(column);
                }
            } else {
                throw new RuntimeException("Component " + c.getName() + " does not have a valid declared primary key");
            }
        }

        // Tackle foreign keys that point to the MASTER table (SR)
        if (!selfReferencingCols.isEmpty()) {
            for (JTableColumn src : selfReferencingCols) {
                if (table.equals(src.getMasterTable())) {
                    src.setForeignKey(table, src.getReferenceColumn(),
                            src.getOnDelete(), src.getOnUpdate());
                    src.setColumnType(src.getMasterTable().getPrimaryKeyColumns().iterator().next().getColumnType());
                    src.setReferencesClass((Class<? extends Component>) c);
                    table.addColumn(src);

                }
            }
        }

        // Put the table in the registry...
        if (db.isVerbose()) {
            System.out.println(table);
        }
        registry.put((Class<? extends Component>) c, table);
    }

    // TODO: Relational tables are not correctly produced in case an entity has a MTM relation with itself.
    // TODO: Discern between the case of Lists and the one of Sets?
    private void relTableCreation() {

        for (Field f : relations) {
            JRelationalTable table = new DerbyRelationalTable();
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            for (Type arg : pt.getActualTypeArguments()) {
                Class carg = (Class) arg;

                table.setTableName(db.getUser(),
                        registry.get(f.getDeclaringClass()).getTableName()
                        + UNDERSCORE + LogicalOperator.AND + UNDERSCORE
                        + registry.get(carg).getTableName() + UNDERSCORE + ON + UNDERSCORE + f.getName());

                Table master = (Table) registry.get((Class<? extends Component>) f.getDeclaringClass());
                Table slave = (Table) registry.get((Class<? extends Component>) carg);

                Set<JTableColumn> masterKeys = master.getPrimaryKeyColumns();
                Set<JTableColumn> slaveKeys = slave.getPrimaryKeyColumns();


                for (JTableColumn col : masterKeys) {
                    TableColumn column = ((TableColumn) col).clone();
                    column.setForeignKey(master, col,
                            OnModification.CASCADE, OnModification.NO_ACTION);
                    try {
                        column.setColumnName(master.getTableName().split(DOT_REG, 0)[1] + UNDERSCORE + col.getColumnName() + "_master");
                    } catch (Exception e) {// TODO: Catch proper exception.
                        column.setColumnName(master.getTableName() + UNDERSCORE + col.getColumnName() + "_master");
                    }
                    column.setAutoGenerated(false);
                    //      column.setField(f);
                    table.addColumn(column);
                    if (isList(f.getType())) {
                        column.setPrimaryKey(false);
                        column.setAutoGenerated(false);
                    }
                }

                for (JTableColumn col : slaveKeys) {
                    TableColumn column = ((TableColumn) col).clone();
                    column.setForeignKey(slave, col,
                            OnModification.CASCADE, OnModification.NO_ACTION);
                    try {
                        column.setColumnName(slave.getTableName().split(DOT_REG, 0)[1] + UNDERSCORE + col.getColumnName() + "_slave");
                    } catch (Exception e) {
                        column.setColumnName(slave.getTableName() + UNDERSCORE + col.getColumnName() + "_slave");
                    }
                    column.setAutoGenerated(false);
                    /*
                     * If the collection is of type 'Set', then the primary key
                     * in the relational table consists of the primary key columns
                     * of the MASTER table only.
                     */
                    if (isList(f.getType())) {
                        column.setPrimaryKey(false);
                        column.setAutoGenerated(false);
                    }
                    table.addColumn(column);
                }
                master.addRelation(table);
                //  slave.addRelation(table);
            }
            table.setOnField(f);
            table.setMasterTable(registry.get(f.getDeclaringClass()));
            /*
             * Create an extra metadata column to store the type of Collection
             * the Relational Table stores
             */
            JTableColumn metaCol = new TableColumn("METACOLUMN");
            metaCol.setColumnType(SQLType.VARCHAR);
            table.addColumn(metaCol);

            if (db.isVerbose()) {
                System.out.println(table);
            }
            registry.setRelationTable(table);
        }
    }
}
