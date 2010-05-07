/**
 *  Class : SubEntityTest
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
package org.kinkydesign.decibell.examples.subclassing;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 *
 * @author chung
 */
public class SubEntityTest {

    private static final Lock lock = new ReentrantLock();
    private static DeciBell db;

    public SubEntityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("decibellTestDB/subclassing/tst38012");


        db.attach(Entity.class);
        db.attach(SubEntity.class);

        db.start();
        System.out.println("Database 'subclassing' created/initialized");
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
    public void testSomeMethod() throws DuplicateKeyException, ImproperRegistration {
        db.start();

        RemoteEntity rem = new RemoteEntity();
        rem.id = 5;
        rem.name = "motsp";

        final SubEntity se = new SubEntity();
        se.id = UUID.randomUUID().toString().replaceAll("-", "");
        se.info = "info";
        se.message = "my msg";
        se.number = 102;
        se.xyz = 1433.3;
        se.remote = rem;
        se.subremote = se;
        lock.lock();
        rem.register(db);
        se.register(db);
        lock.unlock();


        ArrayList<Entity> results = new SubEntity().search(db);
        System.out.println(((SubEntity) results.get(0)).info);
        System.out.println(((SubEntity) results.get(0)).xyz);
        System.out.println(((SubEntity) results.get(0)).message);
        assertTrue(results.size() >= 1);
        assertEquals(results.get(0).message, se.message);
        assertEquals(results.get(0).number, se.number);
        assertTrue(((SubEntity) results.get(0)).xyz == se.xyz);

    }
}
