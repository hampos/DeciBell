/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.examples.selfRef;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 *
 * @author chung
 */
public class SelfRefTest {

    private static DeciBell db = new DeciBell();
    private static final Lock lock = new ReentrantLock();

    public SelfRefTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db.setDbName("decibellTestDB/selfref/self");
        db.attach(SelfRef.class);
        db.start();
        System.out.println("[SUCCESS] Database creation/initialization is successful!");
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
    public void testSelfReferencingEntities() throws DuplicateKeyException, ImproperRegistration {
        SelfRef sr = new SelfRef();
        sr.message = "this is a message";
        sr.x = 12;
        sr.friend = sr;

        lock.lock();
            new SelfRef().delete(db);
            sr.register(db);

            ArrayList<SelfRef> retrievedSelfRefs = new SelfRef().search(db);
            assertEquals(retrievedSelfRefs.size(), 1);
            SelfRef retrSR = retrievedSelfRefs.get(0);
            assertNotNull(retrSR);
            assertNotNull(retrSR.friend.friend);
            assertEquals(retrSR.friend.friend.friend.friend, retrSR);
        lock.unlock();
    }

    @Test
    public void doItAgain() throws DuplicateKeyException, ImproperRegistration{
        lock.lock();
            testSelfReferencingEntities();
            testSelfReferencingEntities();
        lock.unlock();
    }
}
