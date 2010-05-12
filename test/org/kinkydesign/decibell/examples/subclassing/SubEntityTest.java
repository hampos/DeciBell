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
import java.util.Random;
import java.util.Set;
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
        db.setDbName("decibellTestDB/subclassing/e4f4ghsll5");

        db.attach(Entity.class);
        db.attach(SubEntity.class);
        db.attach(RemoteEntity.class);

        db.start();
        System.out.println("*******\n"
                + "CONNECT '" + db.getDatabaseUrl() + "'");
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
    public void testRegisterSubClass() throws DuplicateKeyException, ImproperRegistration {
        lock.lock();
        RemoteEntity rem = new RemoteEntity();
        rem.setId(new Random().nextLong());
        rem.setName("value" + Long.toString(new Random().nextLong()));

        final SubEntity se = new SubEntity();
        se.setId(new Random().nextLong());
        se.setInfo("info");
        se.setMessage("my msg");
        se.setNumber(102);
        se.setXyz(1433.3);
        se.setRemote(rem);
        se.setSubremote(se);
        rem.attemptRegister(db);
        se.register(db);
        lock.unlock();
    }

    @Test
    public void searchForEntity() {
        lock.lock();
        ArrayList<Entity> results = new Entity().search(db);
        System.out.println((results.iterator().next()));
        lock.unlock();
    }

    @Test
    public void searchForSubEntity() {
        try {
            ArrayList<Entity> results = new SubEntity().search(db);
        } catch (UnsupportedOperationException ex) {
            System.err.println("!!!!!SUBCLASSING SUPPORT IS DUE FOR BETA VERSION!!!!!");
        }
        db.disconnect();
    }
}
