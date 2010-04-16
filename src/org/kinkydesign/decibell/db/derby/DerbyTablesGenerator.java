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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.kinkydesign.decibell.annotations.Constraint;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.collections.LogicalOperator;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.TablesGenerator;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.exceptions.NoPrimaryKeyException;

import static org.kinkydesign.decibell.db.derby.util.DerbyKeyWord.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DerbyTablesGenerator extends TablesGenerator {

    public DerbyTablesGenerator(DbConnector connector, Set<Class<? extends Component>> components) {
        super(connector, components);
    }

    public void construct() {
        for (Class<? extends Component> c : components) {
            tableCreation(c);
        }
        relTableCreation();
        Iterator<Table> it = ComponentRegistry.getRegistry(connector).values().iterator();
        while (it.hasNext()) {
            connector.execute(it.next().getCreationSQL());
        }
        it = registry.getRelationTables().iterator();
        while (it.hasNext()) {
            connector.execute(it.next().getCreationSQL());
        }
    }

    private void tableCreation(Class<? extends Component> c) {
        Table table = new DerbyTable();
        if (registry.containsKey( c)) {
            return;
        }
        if (c.getSuperclass() != Component.class && !registry.containsKey((Class<? extends Component>) c.getSuperclass())) {
            tableCreation((Class<? extends Component>) c.getSuperclass());
        }
        table.setTableName(connector.getUser() + DOT + c.getName().replace(DOT, DASH));
        for (Field field : c.getDeclaredFields()) {
            boolean flag = false;
            TableColumn column = new TableColumn();
            column.setColumnName(field.getName());
            column.setColumnType(TypeMap.getSQLType(field.getType()));
            Annotation ann = null;
            if ((ann = field.getAnnotation(PrimaryKey.class)) != null) {
                PrimaryKey pk = (PrimaryKey) ann;
                column.setPrimaryKey(true, pk.autoGenerated());
                flag = true;
            }
            if ((ann = field.getAnnotation(Entry.class)) != null) {
                Entry entry = (Entry) ann;
                column.setUnique(entry.unique());
                column.setNotNull(entry.notNull());
                if (!entry.defaultValue().isEmpty()) {
                    column.setDefaultValue(entry.defaultValue());
                }
                flag = true;
            }
            if ((ann = field.getAnnotation(Constraint.class)) != null) {
                Constraint constraint = (Constraint) ann;
                if (!constraint.low().isEmpty()) {
                    column.setLow(constraint.low());
                }
                if (!constraint.high().isEmpty()) {
                    column.setHigh(constraint.high());
                }
                boolean dom = false;
                for (String s : constraint.domain()) {
                    if (!s.isEmpty()) {
                        dom = true;
                    }
                }
                if (dom) {
                    column.setDomain(constraint.domain());
                }
            }
            if ((ann = field.getAnnotation(ForeignKey.class)) != null) {
                ForeignKey fk = (ForeignKey) ann;
                if (isCollection(field.getType())) {
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
                    flag = false;
                } else {
                    if (isComponent(field.getType())) {
                        if (!registry.containsKey((Class<? extends Component>)field.getType())) {
                            tableCreation((Class<? extends Component>) field.getType());
                        }
                        for (TableColumn col : registry.get((Class<? extends Component>)field.getType()).getPrimaryKeyColumns()) {
                            TableColumn foreignColumn = new TableColumn();
                            if (column.hasDefault()) {
                                foreignColumn.setDefaultValue(column.getDefaultValue());
                            }
                            if (column.isConstrained()) {
                                if (column.hasHigh()) {
                                    foreignColumn.setHigh(column.getHigh());
                                }
                                if (column.hasLow()) {
                                    foreignColumn.setLow(column.getLow());
                                }
                                if (column.hasDomain()) {
                                    foreignColumn.setDomain(column.getDomain());
                                }
                            }
                            foreignColumn.setNotNull(column.isNotNull());
                            foreignColumn.setPrimaryKey(column.isPrimaryKey(), column.isAutoGenerated());
                            foreignColumn.setUnique(column.isUnique());
                            foreignColumn.setColumnName(field.getName() + DASH + col.getColumnName());
                            foreignColumn.setColumnType(col.getColumnType());
                            foreignColumn.setForeignKey(registry.get((Class<? extends Component>)field.getType()),
                                    col, fk.onDelete(), fk.onUpdate());
                            foreignColumn.setReferencesClass((Class<? extends Component>) field.getType());
                            foreignColumn.setField(field);
                            table.addColumn(foreignColumn);
                            flag = false;
                        }
                    } else {
                        throw new ClassCastException("Bad ForeignKey specified - Class:" + field.getDeclaringClass().getName()
                                + " Field:" + field.getName() + " is not a Component");
                    }
                }
            }
            if (flag) {
                column.setField(field);
                table.addColumn(column);
            }
        }
        if (table.getPrimaryKeyColumns().isEmpty()) {
            if (c.getSuperclass() != Component.class) {
                Table superTable = registry.get((Class<? extends Component>) c.getSuperclass());
                for (TableColumn col : superTable.getPrimaryKeyColumns()) {
                    TableColumn column = col.clone();
                    column.setForeignKey(superTable, col, OnModification.CASCADE, OnModification.NO_ACTION);
                    column.setReferencesClass((Class<? extends Component>) c.getSuperclass());
                    table.addColumn(column);
                }
            } else {
                throw new NoPrimaryKeyException("Component " + c.getName() + " does not have a valid declared primary key");
            }
        }
        registry.put((Class<? extends Component>) c, table);
    }

    private void relTableCreation() {
        for (Field f : relations) {
            Table table = new DerbyTable();
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            for (Type arg : pt.getActualTypeArguments()) {
                Class carg = (Class) arg;
                table.setTableName(connector.getUser() + DOT
                        + f.getDeclaringClass().getName().replace(DOT, DASH)
                        + DASH+LogicalOperator.AND+DASH + carg.getName().replace(DOT, DASH)+DASH+ON+DASH+f.getName());
                Table master = registry.get((Class<? extends Component>) f.getDeclaringClass());
                Table slave = registry.get((Class<? extends Component>) carg);
                Set<TableColumn> masterKeys = master.getPrimaryKeyColumns();
                Set<TableColumn> slaveKeys = slave.getPrimaryKeyColumns();
                for (TableColumn col : masterKeys) {
                    TableColumn column = col.clone();
                    column.setForeignKey(master, col,
                            OnModification.CASCADE, OnModification.NO_ACTION);
                    column.setColumnName(master.getTableName().split(DOT_REG,0)[1] + DASH + col.getColumnName());
                    column.setField(f);
                    table.addColumn(column);
                }
                for (TableColumn col : slaveKeys) {
                    TableColumn column = col.clone();
                    column.setForeignKey(slave, col,
                            OnModification.CASCADE, OnModification.NO_ACTION);
                    column.setColumnName(slave.getTableName().split(DOT_REG,0)[1] + DASH + col.getColumnName());
                    column.setField(f);
                    if(isList(f.getType())){
                        column.setPrimaryKey(false, false);
                    }
                    table.addColumn(column);
                }
                master.addRelation(table);
                slave.addRelation(table);
            }
            registry.setRelationTable(table);
        }
    }

}
