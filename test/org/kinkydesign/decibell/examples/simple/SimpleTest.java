/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.examples.simple;

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
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author chung
 */
public class SimpleTest {

    private static DeciBell db = new DeciBell();
    private static final Lock lock = new ReentrantLock();

    public SimpleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
        // Initialize the database...
        db.setDbName("decibellTestDB/simple/s1");
        db.attach(Person.class);
        db.attach(Pet.class);
        lock.lock();
        db.start();
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
    public void testSimpleDbOperations()
            throws NoUniqueFieldException, DuplicateKeyException, ImproperRegistration {

        /**
         * Delete everything from the database!
         */
        new Person().delete(db);
        new Pet().delete(db);

        // Construct a new Pet
        Pet myDog = new Pet();
        myDog.name = "Mitsos";
        myDog.color = "Pink with black dots";
        myDog.numberOfFeet = 4;

        // Contsruct a new Person having the abovementioned Pet
        Person u = new Person();
        u.firstName = "Pantelis";
        u.lastName = "Sopasakis";
        u.id = 102;
        u.friend = myDog;

        /**
         * Attempt to register the
         */
        try {
            myDog.register(db);
            System.out.println("[SUCCESS] Dog Registered");
        } catch (DuplicateKeyException ex) {
            System.err.println("[WARNING] This dog is already registered");
        }

        try {
            u.register(db);
            System.out.println("[SUCCESS] Person Registered");
        } catch (DuplicateKeyException ex) {
            System.err.println("[WARNING] This User is already registered");
        }

        /**
         * Check if the above mentioned data are registered in the database
         * and if these can be successfully retrieved.
         */
        ArrayList<Person> userList = new Person().search(db);
        assertEquals(userList.size(), 1);
        Person retrievedUser = userList.get(0);
        assertEquals(retrievedUser, u);
        assertEquals(retrievedUser.firstName, u.firstName);
        assertEquals(retrievedUser.lastName, u.lastName);

        /**
         * Update the name of the user to John
         */
        u.firstName = "John";
        u.update(db);

        /**
         * Check if the update is successfully performed
         */
        userList = new Person().search(db);
        assertEquals(userList.size(), 1);
        retrievedUser = userList.get(0);
        assertEquals(retrievedUser.firstName, u.firstName);

        /**
         * Delete everything from the database!
         */
        new Person().delete(db);
        new Pet().delete(db);

        /**
         * Verify that everything is now deleted
         */
        userList = new Person().search(db);
        assertEquals(userList.size(), 0);


        /**
         * Register again the user and the pet. No exceptions
         * (like duplicate key) is excpected to be thrown.
         */
        myDog.register(db);
        u.register(db);

    }

    @Test
    public void doItAgain() throws 
            NoUniqueFieldException,
            DuplicateKeyException,
            ImproperRegistration {
        testSimpleDbOperations();
    }
}
