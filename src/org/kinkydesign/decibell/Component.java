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
package org.kinkydesign.decibell;

import com.thoughtworks.xstream.XStream;
import examples.Pool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.query.InsertQuery;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.Infinity;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class Component<T extends Component> {

    public void delete(DeciBell db) throws NoUniqueFieldException {
        Class c = this.getClass();
        ComponentRegistry registry = ComponentRegistry.getRegistry(db.getDbConnector());
        Table table = (Table) registry.get(c);
        StatementPool pool = StatementPool.getPool(db.getDbConnector());
        Entry<PreparedStatement, SQLQuery> entry = pool.getDelete(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery query = entry.getValue();
        try {
            int i = 1;
            for (Proposition p : query.getPropositions()) {
                TableColumn col = p.getTableColumn();
                Field field = col.getField();
                field.setAccessible(true);
                Object obj = null;
                try {
                    obj = field.get(this);
                    if (col.isForeignKey()) {
                        Field f = col.getReferenceColumn().getField();
                        f.setAccessible(true);
                        System.out.println("***" + (Object) f.get(obj));
                        ps.setObject(i, (Object) f.get(obj), col.getColumnType().getType());
                    } else if (obj == null || (col.isTypeNumeric() && obj.equals(0))) {
                        Infinity inf = new Infinity(db.getDbConnector());
                        System.out.println("Column: " + col.getColumnName() + " " + inf.getInfinity(p));
                        ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                    } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        System.out.println(obj);
                        ps.setObject(i, obj, col.getColumnType().getType());
                    } else {
                        XStream xstream = new XStream();
                        String xml = xstream.toXML(obj);
                        System.out.println(xml);
                        ps.setString(i, xml);
                    }
                } catch (NullPointerException ex) {
                    Infinity inf = new Infinity(db.getDbConnector());
                    System.out.println("Column: " + col.getColumnName() + " " + inf.getInfinity(p));
                    ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                }

                i++;
            }
            ps.execute();
            pool.recycleDelete(entry, table);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void register(DeciBell db) throws DuplicateKeyException {
        Class c = this.getClass();
        ComponentRegistry registry = ComponentRegistry.getRegistry(db.getDbConnector());
        Table table = (Table) registry.get(c);
        StatementPool pool = StatementPool.getPool(db.getDbConnector());
        Entry<PreparedStatement, SQLQuery> entry = pool.getRegister(table);
        PreparedStatement ps = entry.getKey();
        try {
            int i = 1;
            for (TableColumn col : table.getTableColumns()) {
                Field field = col.getField();
                field.setAccessible(true);
                if (col.isForeignKey()) {
                    Field f = col.getReferenceColumn().getField();
                    f.setAccessible(true);
                    ps.setObject(i, (Object) f.get(field.get(this)), col.getColumnType().getType());
                } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                    ps.setObject(i, (Object) field.get(this), col.getColumnType().getType());
                } else {
                    XStream xstream = new XStream();
                    String xml = xstream.toXML(field.get(this));
                    System.out.println(xml);
                    ps.setString(i, xml);
                }
                i++;
            }
            ps.execute();
            pool.recycleRegister(entry, table);
            for (JRelationalTable relTable : table.getRelations()) {
                entry = pool.getRegister((JTable) relTable);
                ps = entry.getKey();
                Field field = relTable.getOnField();
                field.setAccessible(true);
                Object obj = null;
                try {
                    obj = field.get(this);
                } catch (NullPointerException ex) {
                    continue;
                }
                if (obj == null) {
                    continue;
                }
                Collection collection = (Collection) obj;
                for (Object o : collection) {
                    i = 1;
                    for (TableColumn col : relTable.getTableColumns()) {
                        Field f = col.getField();
                        f.setAccessible(true);
                        if (col.getReferenceTable().equals(relTable.getMasterTable())) {
                            System.out.println("MASTER " + col.getColumnName());
                            ps.setObject(i, (Object) f.get(this), col.getColumnType().getType());
                        } else {
                            System.out.println("FOREIGN " + col.getColumnName());
                            ps.setObject(i, (Object) f.get(o), col.getColumnType().getType());
                        }
                        i++;
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                pool.recycleRegister(entry, relTable);
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                String message = "Exception due to duplicate key. "
                        + "Object of type "
                        + this.getClass().getCanonicalName()
                        + " with primary key";
                Iterator<TableColumn> it =
                        ComponentRegistry.getRegistry(db.getDbConnector()).get(this.getClass()).getPrimaryKeyColumns().iterator();
                String PKs = "";
                int count = 0;
                while (it.hasNext()) {
                    count++;
                    Field PKfield = it.next().getField();
                    PKfield.setAccessible(true);
                    PKs += PKfield.getName();
                    try {
                        Field f =
                                this.getClass().getDeclaredField(PKfield.getName());
                        f.setAccessible(true);
                        Object valueForField = f.get(this);
                        PKs += " = " + valueForField.toString();
                    } catch (final Exception ex1) {

                        Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null,
                                ex1);
                    }
                    if (it.hasNext()) {
                        PKs += ", ";
                    }
                }
                if (count > 1) {
                    message += "s ";
                }
                message += " " + PKs;

                Set<TableColumn> uniqueCols =
                        ComponentRegistry.getRegistry(db.getDbConnector()).get(this.getClass()).getUniqueColumns();
                count = 0;
                String uniques = "";
                if (!uniqueCols.isEmpty()) {
                    message += " and unique field value";
                    it = uniqueCols.iterator();
                    while (it.hasNext()) {
                        count++;
                        Field uniqueField = it.next().getField();
                        uniqueField.setAccessible(true);
                        uniques += uniqueField.getName();
                        try {
                            Field f =
                                    this.getClass().getDeclaredField(uniqueField.getName());
                            f.setAccessible(true);
                            Object valueForField = f.get(this);
                            uniques += " = " + valueForField.toString();
                        } catch (final Exception ex1) {

                            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null,
                                    ex1);
                        }
                        if (it.hasNext()) {
                            uniques += ", ";
                        }
                    }
                    if (count > 1) {
                        message += "s ";
                    }
                    message += " " + uniques;
                }
                throw new DuplicateKeyException(message);
            }
        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);
        }



    }

    public ArrayList<T> search(DeciBell db) {
        Class c = this.getClass();
        ComponentRegistry registry = ComponentRegistry.getRegistry(db.getDbConnector());
        Table table = (Table) registry.get(c);
        StatementPool pool = StatementPool.getPool(db.getDbConnector());
        Entry<PreparedStatement, SQLQuery> entry = pool.getSearch(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery query = entry.getValue();
        try {
            int i = 1;
            for (Proposition p : query.getPropositions()) {
                TableColumn col = p.getTableColumn();
                Field field = col.getField();
                field.setAccessible(true);
                Object obj = null;
                try {
                    obj = field.get(this);
                    if (col.isForeignKey()) {
                        Field f = col.getReferenceColumn().getField();
                        f.setAccessible(true);
                        System.out.println("***" + (Object) f.get(obj));
                        ps.setObject(i, (Object) f.get(obj), col.getColumnType().getType());
                    } else if (obj == null || (col.isTypeNumeric() && obj.equals(0))) {
                        Infinity inf = new Infinity(db.getDbConnector());
                        System.out.println("Column: " + col.getColumnName() + " " + inf.getInfinity(p));
                        ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                    } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        System.out.println(obj);
                        ps.setObject(i, obj, col.getColumnType().getType());
                    } else {
                        XStream xstream = new XStream();
                        String xml = xstream.toXML(obj);
                        System.out.println(xml);
                        ps.setString(i, xml);
                    }
                } catch (NullPointerException ex) {
                    Infinity inf = new Infinity(db.getDbConnector());
                    System.out.println("Column: " + col.getColumnName() + " " + inf.getInfinity(p));
                    ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                }
                i++;
            }
            ResultSet rs = ps.executeQuery();
            pool.recycleDelete(entry, table);
            ArrayList<T> resultList = new ArrayList<T>();

        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public ArrayList<T> search(String... fields) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update() throws NoUniqueFieldException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
