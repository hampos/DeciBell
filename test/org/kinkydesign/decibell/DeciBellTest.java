/**
 *  Class : DeciBellTest
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

package org.kinkydesign.decibell;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.examples.simple.Person;
import org.kinkydesign.decibell.examples.simple.Pet;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;

/**
 *
 * @author chung
 */
public class DeciBellTest {

    private static DeciBell db = new DeciBell();
    private static final Lock lock = new ReentrantLock();
    private static final int N_TEST = 1;
    

    public DeciBellTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Initialize the database...
        lock.lockInterruptibly();
        db = new DeciBell();       
        db.setDbName("decibellTestDB/dbcreation42");
        db.attach(Person.class);
        db.attach(Pet.class);
        db.start();
        assertTrue(db.isConnected());
        lock.unlock();
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
    public synchronized void testDataBaseOnOff() {
        lock.lock();
        db.reset();        
        assertTrue(db.isConnected());
        db.stop();        
        assertFalse(db.getDbConnector().isServerRunning());
        assertFalse(db.isConnected());
        try {
            db.start();
        } catch (ImproperDatabaseException ex) {
            fail();
        }
        assertTrue(db.isConnected());
        assertTrue(db.getDbConnector().isServerRunning());
        lock.unlock();
    }

    @Test
    public synchronized void onOffOnOff() throws InterruptedException{
        lock.lock();
        for (int i=0;i<N_TEST;i++){
            testDataBaseOnOff();
        }        
        lock.unlock();
    }

    

}