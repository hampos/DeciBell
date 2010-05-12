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
package org.kinkydesign.decibell.examples.enm;

import java.util.ArrayList;
import java.util.UUID;
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
public class ChungTest {

    private static DeciBell db;
    private static final Lock lock = new ReentrantLock();

    public ChungTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.attachFromPackages(
                "org.kinkydesign.decibell.examples.enm",
                "org.kinkydesign.decibell.examples.yaqp");
        db.setDbName("decibellTestDB/chung/chungtest01g");
        db.start();
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
    public void testEnumeration() throws DuplicateKeyException, ImproperRegistration {
        lock.lock();
        new Chung().delete(db);

        Chung chung = new Chung(UUID.randomUUID().toString(), Chung.STATUS.HIGH);
        ArrayList<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("def");
        chung.setXmlization(list);
        chung.register(db);
        chung = new Chung(UUID.randomUUID().toString(), Chung.STATUS.LOW);
        chung.setXmlization(list);
        chung.register(db);
        chung = new Chung(UUID.randomUUID().toString(), Chung.STATUS.LOW);
        chung.setXmlization(list);
        chung.register(db);

        assertTrue(new Chung(null, Chung.STATUS.MEDIUM).find(db).size() == 0);
        assertNotNull(new Chung().find(db).get(0).getStatus());
        lock.unlock();
    }

     @Test
     public void doItAgain() throws DuplicateKeyException, ImproperRegistration{
         lock.lock();
         testEnumeration();
         lock.unlock();
     }


    
}
