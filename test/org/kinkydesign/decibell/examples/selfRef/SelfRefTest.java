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
        db.setDbName("decibellTestDB/selfref/elf09as5");
        db.attach(SelfRef.class);
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
    public void testSelfReferencingEntities() throws DuplicateKeyException, ImproperRegistration {
        SelfRef sr = new SelfRef();
        sr.setMessage("this is a message");
        sr.setX(12);
        sr.setFriend(sr);

        SelfRef sr2 = new SelfRef();
        sr2.setX(4523);
        sr2.setMessage("afff");
        sr2.setFriend(sr);

        lock.lock();
        new SelfRef().delete(db);
        sr.register(db);
        sr2.register(db);

        ArrayList<SelfRef> retrievedSelfRefs = new SelfRef().find(db);
        assertEquals(retrievedSelfRefs.size(), 2);
        assertTrue(retrievedSelfRefs.contains(sr));
        assertTrue(retrievedSelfRefs.contains(sr2));

        SelfRef retrSR = retrievedSelfRefs.get(0);
        assertNotNull(retrSR);
        assertNotNull(retrSR.getFriend().getFriend());
        assertEquals(retrSR.getFriend().getFriend().getFriend().getFriend(), sr);
        if (retrSR.equals(sr)){
            assertEquals(retrSR.getFriend(), sr.getFriend());
        }else if (retrSR.equals(sr2)){
            assertEquals(retrSR.getFriend(), sr2.getFriend());
        }else{
            fail();
        }

        retrSR = retrievedSelfRefs.get(1);
        assertNotNull(retrSR);
        assertNotNull(retrSR.getFriend().getFriend());
        assertEquals(retrSR.getFriend().getFriend().getFriend().getFriend(), sr);
        if (retrSR.equals(sr)){
            assertEquals(retrSR.getFriend(), sr.getFriend());
        }else if (retrSR.equals(sr2)){
            assertEquals(retrSR.getFriend(), sr2.getFriend());
        }else{
            fail();
        }

        assertEquals(sr.attemptRegister(db), 1);
        assertEquals(sr2.attemptRegister(db), 1);
        System.out.println("CONNECT '"+db.getDatabaseUrl()+"';");
        lock.unlock();
    }

    @Test
    public void doItAgain() throws DuplicateKeyException, ImproperRegistration {
        lock.lock();
        testSelfReferencingEntities();
        testSelfReferencingEntities();
        lock.unlock();
    }
}
