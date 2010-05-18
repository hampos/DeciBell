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
package org.kinkydesign.decibell.exceptions;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.DbConnector;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;

/**
 * A Duplicate Key Exception is a kind of exception thrown if one attempts to violate
 * a primary key or unique field constraint violation.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DuplicateKeyException extends DeciBellException {

    String explanation = "Unknown cause of Exception";

    /**
     * Creates a new instance of <code>DuplicateKeyException</code> without detail message.
     * The detail message is <code>null</code>.
     */
    public DuplicateKeyException() {
        super();
    }

    /**
     * Constructs an instance of <code>DuplicateKeyException</code> with the
     * specified detail message.
     * @param msg
     *      The detail message.
     */
    public DuplicateKeyException(String msg) {
        super(msg);
        this.explanation = msg;
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
        explanation = cause.getMessage();
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
        this.explanation = message;
    }

    public DuplicateKeyException(Object obj, DbConnector con, Throwable ex) {
        super(ex);
        StringBuffer message = new StringBuffer();
        message.append("Exception due to duplicate key. "
                + "Object of type ");
        message.append(obj.getClass().getCanonicalName());
        Set<JTableColumn> uniqueCols =
                ComponentRegistry.getRegistry(con).get(obj.getClass()).getUniqueColumns();
        int count = 0;
        StringBuffer uniques = new StringBuffer();
        if (!uniqueCols.isEmpty()) {
            message.append(" and unique/primary field value");
            Iterator<JTableColumn> it = uniqueCols.iterator();
            while (it.hasNext()) {
                count++;
                Field uniqueField = it.next().getField();
                uniqueField.setAccessible(true);
                uniques.append(uniqueField.getName());
                try {
                    Field f =
                            obj.getClass().getDeclaredField(uniqueField.getName());
                    f.setAccessible(true);
                    Object valueForField = f.get(obj);
                    uniques.append(" = " + valueForField);
                } catch (final Exception ex1) {
                    throw new RuntimeException(ex1);
                }
                if (it.hasNext()) {
                    uniques.append(", ");
                }
            }
            if (count > 1) {
                message.append("s ");
            }
            message.append(" ");
            message.append(uniques);
        }
        this.explanation = message.toString();
    }

    @Override
    public String getMessage() {
        return this.explanation;
    }
}
