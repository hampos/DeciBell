/**
 *  Class : MultiFKTest
 *  Date  : May 2, 2010
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

package org.kinkydesign.decibell.examples.multifk;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 *
 * @author chung
 */
public class MultiFKTest {

    private static DeciBell db = new DeciBell();
    private static final Lock lock = new ReentrantLock();

    public MultiFKTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("decibellTestDB/multifk/masl");
        db.attach(Master.class);
        db.attach(Slave.class);
        lock.lock();
        db.start();
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
    public void testRegisterMasterSlave() throws
            DuplicateKeyException, ImproperRegistration {
        Slave slave = new Slave("John", "Smith", 102);
        Master master = new Master("Master", slave);

        new Master().delete(db);
        new Slave().delete(db);

        lock.lock();
        try {
            slave.register(db);
            master.register(db);
        } catch (DuplicateKeyException ex) {
            fail("Database should be empty!");
        }

        ArrayList<Master> retrievedMasters = new Master().search(db);
        lock.unlock();

        assertEquals(retrievedMasters.size(), 1);
        assertNotNull(retrievedMasters.get(0));
        assertEquals(retrievedMasters.get(0).slave, slave);
        assertEquals(retrievedMasters.get(0).slave.firstName, "John");
        assertTrue(retrievedMasters.get(0).slave.date.before(new Date(System.currentTimeMillis())));
        
    }

    @Test
    public void doItAgain() throws DuplicateKeyException, ImproperRegistration{
        lock.lock();
        testRegisterMasterSlave();
        lock.unlock();
    }
}
