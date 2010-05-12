/**
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
package org.kinkydesign.decibell.examples.simple;

import java.util.ArrayList;
import java.util.Set;
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
        db.setDbName("decibellTestDB/simple/s94af4s");
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
        myDog.setName("Mitsos");
        myDog.setColor("Pink with black dots");
        myDog.setNumberOfFeet(4);

        // Contsruct a new Person having the abovementioned Pet
        Person u = new Person();
        u.setFirstName("Pantelis");
        u.setLastName("Sopasakis");
        u.setId(102);
        u.setFriend(myDog);

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
        Set<Person> userList = new Person().search(db);
        assertEquals(userList.size(), 1);
        Person retrievedUser = userList.iterator().next();
        assertEquals(retrievedUser, u);
        assertEquals(retrievedUser.getFirstName(), u.getFirstName());
        assertEquals(retrievedUser.getLastName(), u.getLastName());

        /**
         * Update the name of the user to John
         */
        u.setFirstName("John");
        u.update(db);

        /**
         * Check if the update is successfully performed
         */
        userList = new Person().search(db);
        assertEquals(userList.size(), 1);
        retrievedUser = userList.iterator().next();
        assertEquals(retrievedUser.getFirstName(), u.getFirstName());

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

   // @Test
    public void doItAgain() throws
            NoUniqueFieldException,
            DuplicateKeyException,
            ImproperRegistration {
        testSimpleDbOperations();
    }
}
