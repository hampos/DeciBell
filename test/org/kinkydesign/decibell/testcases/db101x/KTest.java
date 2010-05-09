/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kinkydesign.decibell.testcases.db101x;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 *
 * @author chung
 */
public class KTest {

    private static DeciBell db;
    private static final Lock lock = new ReentrantLock();

    public KTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("testDB/db101x/yt47d");

        db.attach(K.class);
        db.attach(L.class);

        lock.lock();
        db.start();
        System.out.println("CONNECT '"+db.getDatabaseUrl()+"';");
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
    public void testdb101x() throws ImproperRegistration {
        L b = new L("b");
        b.attemptRegister(db);

        L b1 = new L("b1");
        b1.attemptRegister(db);

        L b2 = new L("b2");
        b2.attemptRegister(db);

        L b3 = new L("b3");
        b3.attemptRegister(db);

        K a = new K(b,null);
        a.attemptRegister(db);

        K a1 = new K(b1,"haha");
        a1.attemptRegister(db);

        K a2 = new K(b2,"haha");
        a2.attemptRegister(db);

        K a3 = new K(b3,"hehe");
        a3.attemptRegister(db);

        ArrayList<K> retrievedA = new K().search(db);
        assertTrue(retrievedA.contains(a1));
        assertTrue(retrievedA.contains(a2));
        assertTrue(retrievedA.contains(a3));

        ArrayList<L> retrievedB = new L().search(db);
        assertTrue(retrievedB.contains(b1));
        assertTrue(retrievedB.contains(b2));
        assertTrue(retrievedB.contains(b3));

        ArrayList<K> searchSpecific = a3.search(db);
        assertEquals(searchSpecific.size(), 1);
        assertEquals(searchSpecific.get(0), a3);

        ArrayList<L> searchB = b2.search(db);
        assertEquals(searchB.size(), 1);
        assertEquals(searchB.get(0), b2);

        ArrayList<K> searchOperation = new K(null, "haha").search(db);
        assertTrue(searchOperation.contains(a1));
        assertTrue(searchOperation.contains(a2));
        assertEquals(searchOperation.size(),2);
    }

    @Test
    public void repeatTest() throws ImproperRegistration {
        testdb101x();
    }


}