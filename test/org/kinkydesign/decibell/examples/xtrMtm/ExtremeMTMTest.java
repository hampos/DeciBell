/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        db.setDbName("decibellTestDB/mtm/extreme/1");
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
        s1.someCollection = col1;
        s1.register(db);

        Slave s2 = new Slave("s2", "s2");
        Collection<Something> col2 = new HashSet<Something>();
        col2.add(sth1);
        col2.add(sth2);
        s2.someCollection = col1;
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
        assertNotNull(retrievedMaster.firstList);
        assertNotNull(retrievedMaster.secondList);
        assertEquals(retrievedMaster.secondList.iterator().next().
                someCollection.iterator().next().name,sth2.name);
        

    }

}