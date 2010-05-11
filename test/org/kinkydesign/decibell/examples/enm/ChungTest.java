/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.examples.enm;

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

        assertTrue(new Chung(null, Chung.STATUS.MEDIUM).search(db).size() == 0);
        assertNotNull(new Chung().search(db).get(0).getStatus());
        lock.unlock();
    }

     @Test
     public void doItAgain() throws DuplicateKeyException, ImproperRegistration{
         lock.lock();
         testEnumeration();
         lock.unlock();
     }


    
}
