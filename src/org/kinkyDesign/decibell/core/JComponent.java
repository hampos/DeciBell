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
package org.kinkydesign.decibell.core;

import java.util.ArrayList;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JComponent {

    /**
     * Registers the component in the database.
     */
    void register() throws DuplicateKeyException;

    /**
     * Get all the components from the database which resemble the given component.
     * The current component is used as a prototype for the performed search. Any
     * fields set to <code>null</code> are ommited throughout the search. This search is
     * based on prepared statements for increased performance and security. These statements
     * are prepared upon the startup of decibell.
     * @return List of results
     */
    ArrayList<Component> search();

    /**
     * Returns only the prescribed fields of the component, that is it performs a more
     * conservative search over these fields only and overrides all other fields of the
     * object. Note that the search is not based on prepared statements but on statements,
     * created on the fly, thus no performance is guarranteed.
     * @param fields
     * @return List of results.
     */
    ArrayList<Component> search(String... fields);

    /**
     * Performs an update on the specified component. First, a database search is performed,
     * and if the current componentm uniquely corresponbds to a certain database entry, it is
     * updated with the new data. If some field in the current component is set to null,
     * this is reproduced in the database by setting the coresponding field to null.
     * This operation is based on prepared statements.
     */
    void update() throws NoUniqueFieldException;

    /**
     * Deletes the current component from the database. At least one valid unique field must
     * be specified in order for the delete operation to take place.
     * This operation is based on prepared statements.
     */
    void delete() throws NoUniqueFieldException;
    
}