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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.collections.TypeMap;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;
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
        Table table = registry.get(c);
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
        Table table = registry.get(c);
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
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 23505) {
                throw new DuplicateKeyException();
            } else {
                throw new RuntimeException(ex);
            }
        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);
        }



    }

    public ArrayList<T> search() {
        try {
            Class c = this.getClass();
            Field[] fields = c.getDeclaredFields();
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            fields[1].set(obj, 3);
            ArrayList<T> list = new ArrayList<T>();
            list.add((T) obj);
            return list;
        } catch (InstantiationException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
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
