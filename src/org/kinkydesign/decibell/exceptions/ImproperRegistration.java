/**
 *  Class : ImproperRegistration
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

package org.kinkydesign.decibell.exceptions;

import java.security.PrivilegedActionException;


/**
 * <p  align="justify" style="width:60%">
 * Exception thrown in case an 'improper' entity is submitted for registration in
 * a database. An entity is improper for registration when it holds a <code>null</code>
 * Collection-type field.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ImproperRegistration extends DeciBellException {

    /**
     * <p  align="justify" style="width:60%">
     * Creates a new instance of <code>ImproperRegistration</code> without 
     * detail message. The detail message is initialized as <code>null</code>.
     * </p>
     */
    public ImproperRegistration() {
        
    }


    /**
     * <p  align="justify" style="width:60%">
     * Constructs an instance of <code>ImproperRegistration</code> with the specified 
     * detail message.
     * </p>
     * @param msg 
     *      The detail message.
     */
    public ImproperRegistration(String msg) {
        super(msg);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Constructs an instance of <code>ImproperRegistration</code>
     * with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}). This is accomplished with the
     * invokation of the constructor <code>Exception(Throwable cause)</code> in Class
     * <code>java.lang.Exception</code>.
     * </p>
     *
     * @param  cause 
     *         The cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public ImproperRegistration(Throwable cause) {
        super(cause);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Combination of the constructors accepting a detail message and a cause for
     * the exception.
     * </p>
     * @param cause
     *         The cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param message
     *      The detail message.
     */
    public ImproperRegistration(String message, Throwable cause) {
        super(message, cause);
    }

    


}
