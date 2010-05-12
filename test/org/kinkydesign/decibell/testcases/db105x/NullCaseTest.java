/**
 *  Class : NullCaseTest
 *  Date  : May 11, 2010
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

package org.kinkydesign.decibell.testcases.db105x;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DeciBellException;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author chung
 */
public class NullCaseTest {

    public NullCaseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws DeciBellException {
        DeciBell db = new DeciBell();
        db.setDbName("decibellTestDB/nullcase/nc4");

        db.attach(NullCase.class);
        db.start();

        NullCase nc = new NullCase(null, null);
        nc.delete(db);

        nc.attemptRegister(db);
        assertEquals(nc.attemptRegister(db), 1);
        NullCase retrievedCase = nc.search(db).get(0);
        assertNotNull(retrievedCase);
        assertEquals(retrievedCase.getMyEntry(),"__NULL__");
        assertEquals(retrievedCase.getUid(),"__NULL__");

        nc.delete(db);
        assertEquals(nc.search(db).size(),0);
        nc.attemptRegister(db);
        nc = new NullCase("__NULL__", "message");
        nc.update(db);

        retrievedCase = nc.search(db).get(0);
        assertNotNull(retrievedCase);
        assertEquals(retrievedCase.getMyEntry(), nc.getMyEntry());
        assertEquals(retrievedCase.getUid(), nc.getUid());
    }

}