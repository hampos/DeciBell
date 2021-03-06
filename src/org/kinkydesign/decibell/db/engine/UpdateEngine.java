/**
 *  Class : UpdateEngine
 *  Date  : May 1, 2010
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
package org.kinkydesign.decibell.db.engine;

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.StatementPool;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.interfaces.JRelationalTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.Infinity;
import org.kinkydesign.decibell.db.util.Pair;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UpdateEngine {

    private final DeciBell db;
    private final ComponentRegistry registry;
    private final StatementPool pool;

    public UpdateEngine(final DeciBell db) {
        this.db = db;
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
        pool = StatementPool.getPool(db.getDbConnector());
    }

    public void update(Component whatToUpdate) throws NoUniqueFieldException, DuplicateKeyException {
        try {
            doUpdate(whatToUpdate);
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                throw new DuplicateKeyException(whatToUpdate, db.getDbConnector(), ex);
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    // TODO: Discuss how to handle NULLs
    public void doUpdate(Component whatToUpdate) throws NoUniqueFieldException, DuplicateKeyException, SQLException {
        Class c = whatToUpdate.getClass();
        Table table = (Table) registry.get(c);

        try {

            /*
             * Updating contained components first
             */
            for (Set<JTableColumn> group : table.getForeignColumnsByGroup()) {
                Field fkField = group.iterator().next().getField();
                fkField.setAccessible(true);
                Object obj = fkField.get(whatToUpdate);
                Component component = (Component) obj;
                if (!whatToUpdate.equals(component)) {
                    component.update(db);
                }
            }

            Pair<PreparedStatement, SQLQuery> entry = pool.getUpdate(table);
            PreparedStatement ps = entry.getKey();
            SQLQuery query = entry.getValue();

            /*
             * Updating normal entries
             */
            int ps_INDEX = 1;
            for (Proposition p : query.getPropositions()) {
                JTableColumn col = p.getTableColumn();
                Field field = col.getField();
                field.setAccessible(true);
                Object obj = null;
                try {
                    obj = field.get(whatToUpdate);
                    if (col.isForeignKey()) {
                        Field f = col.getReferenceColumn().getField();
                        f.setAccessible(true);
                        ps.setObject(ps_INDEX, (Object) f.get(obj), col.getColumnType().getType());
                    } else if (obj == null
                            || (col.isTypeNumeric() && ((Double.parseDouble(obj.toString())) == Double.parseDouble(col.getNumericNull())))) {
                        ps.setNull(ps_INDEX, col.getColumnType().getType());
                    } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        ps.setObject(ps_INDEX, obj, col.getColumnType().getType());
                    } else {
                        XStream xstream = new XStream();
                        String xml = xstream.toXML(obj);
                        ps.setString(ps_INDEX, xml);
                    }
                } catch (NullPointerException ex) {
                    ps.setNull(ps_INDEX, col.getColumnType().getType());
                }
                ps_INDEX++;
            }
            ps.execute();
            pool.recycleUpdate(entry, table);

            /*
             * Deleting entries from relational table first
             */
            for (JRelationalTable relTable : table.getRelations()) {
                entry = pool.getDelete(relTable);
                ps = entry.getKey();
                query = entry.getValue();
                ps_INDEX = 1;
                for (Proposition p : query.getPropositions()) {
                    JTableColumn col = p.getTableColumn();
                    Field f = col.getField();
                    f.setAccessible(true);
                    if (col.getReferenceTable().equals(relTable.getMasterTable())) {
                        ps.setObject(ps_INDEX, (Object) f.get(whatToUpdate), col.getColumnType().getType());
                    } else {
                        Infinity inf = new Infinity(db.getDbConnector());
                        ps.setObject(ps_INDEX, inf.getInfinity(p), col.getColumnType().getType());
                    }
                    ps_INDEX++;
                }
                ps.execute();
                pool.recycleDelete(entry, relTable);

                /*
                 * Registering new entries to relational table
                 */
                entry = pool.getRegister(relTable);
                ps = entry.getKey();
                Field field = relTable.getOnField();
                field.setAccessible(true);
                Object obj = null;
                try {
                    obj = field.get(whatToUpdate);
                } catch (NullPointerException ex) {
                    continue;
                }
                if (obj == null) {
                    continue;
                }
                Collection collection = (Collection) obj;
                for (Object o : collection) {
                    ps_INDEX = 1;
                    for (JTableColumn col : relTable.getTableColumns()) {
                        if (col.getColumnName().equals("METACOLUMN")) {
                            Field f = relTable.getOnField();
                            ps.setObject(ps_INDEX, (Object) obj.getClass().getName(), SQLType.VARCHAR.getType());
                            ps_INDEX++;
                            continue;
                        }
                        Field f = col.getField();
                        f.setAccessible(true);
                        if (col.getReferenceTable().equals(relTable.getMasterTable())) {
                            ps.setObject(ps_INDEX, (Object) f.get(whatToUpdate), col.getColumnType().getType());
                        } else {
                            ps.setObject(ps_INDEX, (Object) f.get(o), col.getColumnType().getType());
                        }
                        ps_INDEX++;
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                pool.recycleRegister(entry, relTable);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }


    }
}
