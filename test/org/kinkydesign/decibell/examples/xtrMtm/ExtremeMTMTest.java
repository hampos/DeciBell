/**
 *  Class : ExtremeMTMTest
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


package org.kinkydesign.decibell.examples.xtrMtm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
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
public class ExtremeMTMTest {

    private static final Lock lock = new ReentrantLock();
    private static DeciBell db;

    public ExtremeMTMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("decibellTestDB/mtm/xtr4");
        db.attach(Master.class);
        db.attach(Slave.class);
        db.attach(Something.class);
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
    public void testSomeMethod() throws DuplicateKeyException, ImproperRegistration {

        new Master().delete(db);
        new Slave().delete(db);
        new Something().delete(db);

        
        Something sth1 = new Something("sth1", "x1");
        Something sth2 = new Something("sth2", "x2");
        sth1.register(db);
        sth2.register(db);

        Slave s1 = new Slave("s1", "s1");
        Collection<Something> col1 = new HashSet<Something>();
        col1.add(sth1);
        col1.add(sth2);
        s1.setSomeCollection(col1);
        s1.register(db);

        Slave s2 = new Slave("s2", "s2");
        Collection<Something> col2 = new HashSet<Something>();
        col2.add(sth1);
        col2.add(sth2);
        s2.setSomeCollection(col2);
        s2.register(db);

        Collection<Slave> sc = new LinkedList<Slave>();
        sc.add(s2);
        sc.add(s1);
        Master m = new Master("masterName", sc, sc);

        m.register(db);


        ArrayList<Master> retrievedMasters = new Master().search(db);
        assertEquals(retrievedMasters.size(), 1);

        Master retrievedMaster = retrievedMasters.get(0);

        assertEquals(retrievedMaster, m);
        assertNotNull(retrievedMaster.getFirstList());
        assertNotNull(retrievedMaster.getSecondList());
        assertEquals(retrievedMaster.getSecondList().iterator().next().
                getSomeCollection().iterator().next().getName(),sth2.getName()  );
        

    }

}