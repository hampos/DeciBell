/**
 *  Class : NumericDefaultTest
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

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DeciBellException;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;

/**
 *
 * @author chung
 */
public class NumericDefaultTest {

    public NumericDefaultTest() {
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
        db.setDbName("decibellTestDB/numericDefault");
        db.attach(NumericDefault.class);
        db.start();

        /*
         * Setting a numeric field to its numeric-null value, is like setting it
         * to NULL. This will be translated into DEFAULT in the database INSERT
         * operation.
         */
        new NumericDefault().delete(db);
        NumericDefault nd = new NumericDefault(null, 0, 0);
        nd.attemptRegister(db);
        nd = new NumericDefault("s", 1, 1);
        nd.register(db);
        ArrayList<NumericDefault> retrievedObjs = new NumericDefault().search(db);
        assertEquals(retrievedObjs.size(), 2);
        assertEquals(retrievedObjs.get(0).getId(),"XYZ");
        assertTrue(retrievedObjs.get(0).getNumDouble()==14.25);
        assertTrue(retrievedObjs.get(0).getNumInteger()==512);
        assertEquals(retrievedObjs.get(1).getId(),"s");
        assertTrue(retrievedObjs.get(1).getNumDouble()==1);
        assertTrue(retrievedObjs.get(1).getNumInteger()==1);
    }

}