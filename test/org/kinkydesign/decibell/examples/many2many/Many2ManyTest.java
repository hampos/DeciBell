/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.examples.many2many;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author chung
 */
public class Many2ManyTest {

    private static final Lock lock = new ReentrantLock();
    private static DeciBell db;

    public Many2ManyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("decibellTestDB/manytomany/y");

        db.attach(Person.class);
        db.attach(Pet.class);

        db.start();

        System.out.println("* connect '" + db.getDatabaseUrl() + "'");
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
    public void testSomeMethod() throws ImproperRegistration {

        Pet dog = new Pet();
        dog.name = "Sam";
        dog.color = "black";

        Pet cat = new Pet();
        cat.color = "white";
        cat.name = "Sylvester";

        Pet cock = new Pet();
        cock.color = "brown/white";
        cock.name = "George";

        List<Pet> petList = new ArrayList<Pet>();
        petList.add(cat);
        petList.add(dog);

        Collection<Pet> yourList = new LinkedHashSet<Pet>();
        yourList.add(cat);
        yourList.add(cock);

        Person me = new Person();
        me.id = 10;
        me.petList = petList;
        petList.remove(dog);
        me.otherList = petList;

        Person you = new Person();
        you.id = 11;
        you.petList = yourList;
        you.otherList = petList;

        lock.lock();
        new Person().delete(db);
        new Pet().delete(db);

        try {
            cat.register(db);
            dog.register(db);
            cock.register(db);
            me.register(db);
            you.register(db);
        } catch (final DuplicateKeyException ex) {
            fail("No person or pet should be in the database!");
        }

        ArrayList<Person> retrievedPersons = new Person().search(db);
        assertEquals(retrievedPersons.size(), 2);

        Person p1 = retrievedPersons.get(0);
        Person p2 = retrievedPersons.get(1);

        assertEquals(p1, me);
        assertEquals(p1.petList, me.petList);
        assertEquals(p1.otherList, me.otherList);
        assertEquals(p2, you);
        assertEquals(p2.x, you.x);

        /*
         * Check if update works....
         */
        p1.petList.add(dog);
        try {
            p1.update(db);
            retrievedPersons = new Person().search(db);
            p1 = retrievedPersons.get(0);
            assertTrue(p1.petList.contains(dog));
        } catch (NoUniqueFieldException ex) {
            Logger.getLogger(Many2ManyTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(Many2ManyTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        lock.unlock();
    }    

    @Test
    public void doItAgain() throws ImproperRegistration {
        lock.lock();
        testSomeMethod();
        testSomeMethod();
        lock.unlock();
    }

    @Test
    public void addingNullCollection() throws DuplicateKeyException {
        
        lock.lock();

        new Person().delete(db);
        new Pet().delete(db);

        Person me = new Person();
        me.id = 3;
        me.x = "message";
        me.petList = null;

        try {
            me.register(db);
        } catch (Throwable ex) {
            if (ex instanceof ImproperRegistration){
                lock.unlock();
                return;
            }
        }
        lock.unlock();
        fail("SHOULD HAVE FAILED!");
    }
}
