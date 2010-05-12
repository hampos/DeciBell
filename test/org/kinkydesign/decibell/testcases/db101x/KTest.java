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
        db.setDbName("testDB/db101x/akfjsee");

        db.attach(K.class);
        db.attach(L.class);
        db.attach(R.class);

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
        R remote1 = new R();
        remote1.id = "xyz";
        remote1.xxx = "message1";
        remote1.attemptRegister(db);

        R remote2 = new R();
        remote2.id = "aaa";
        remote2.xxx = "message....2";
        remote2.attemptRegister(db);

        L l0 = new L("l_0");
        l0.remote = remote1;
        l0.attemptRegister(db);


        L l1 = new L("l_1");
        l1.remote = remote1;
        l1.attemptRegister(db);

        L l2 = new L("l_2");
        l2.remote = remote1;
        l2.attemptRegister(db);

        L l3 = new L("l_3");
        l3.remote = remote2;
        l3.attemptRegister(db);

        K k0 = new K(l0,null); // null will be registered as my_entry
        k0.attemptRegister(db);

        K k1 = new K(l1,"haha");
        k1.attemptRegister(db);

        K k2 = new K(l2,"haha");
        k2.attemptRegister(db);

        K k3 = new K(l3,"hehe");
        k3.attemptRegister(db);

        ArrayList<K> retrievedA = new K().search(db);
        assertTrue(retrievedA.contains(k1));
        assertTrue(retrievedA.contains(k2));
        assertTrue(retrievedA.contains(k3));

        ArrayList<L> retrievedB = new L().search(db);
        assertTrue(retrievedB.contains(l1));
        assertTrue(retrievedB.contains(l2));
        assertTrue(retrievedB.contains(l3));

        ArrayList<K> searchSpecific = k3.search(db);
        assertEquals(searchSpecific.size(), 1);
        assertEquals(searchSpecific.get(0), k3);

        ArrayList<L> searchB = l2.search(db);
        assertEquals(searchB.size(), 1);
        assertEquals(searchB.get(0), l2);

        ArrayList<K> searchOperation = new K(null, "haha").search(db);
        assertTrue(searchOperation.contains(k1));
        assertTrue(searchOperation.contains(k2));
        assertEquals(searchOperation.size(),2);

        K kSearch = new K();
        L lSearch = new L();
        lSearch.remote = remote2;
        kSearch.setL(lSearch);

//        for (K k : kSearch.search(db)){
//            k.print(System.out);
//        }

        assertEquals(1,kSearch.find(db).size());  // Here 'find' has proven to solve some previoud issues
    }

    //@Test
    public void repeatTest() throws ImproperRegistration {
        testdb101x();
    }


}