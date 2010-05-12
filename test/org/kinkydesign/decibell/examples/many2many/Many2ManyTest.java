/**
 *  Class : Many2ManyTest
 *  Date  : Apr 24, 2010
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
        db.setDbName("decibellTestDB/manytomany/haha2");

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
    public void testManyToMany() throws ImproperRegistration {

        Pet dog = new Pet("Sam", "black");
        Pet cat = new Pet("white", "Sylvester");
        Pet cock = new Pet("brown/white", "George");

        List<Pet> petList = new ArrayList<Pet>();
        petList.add(cat);
        petList.add(dog);

        Collection<Pet> yourList = new LinkedHashSet<Pet>();
        yourList.add(cat);
        yourList.add(cock);

        Person me = new Person("agrd", petList, petList);
        petList.remove(dog);
        me.setOtherList(petList);

        Person you = new Person("avfs", yourList, petList);

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
            ex.printStackTrace();
            fail("No person or pet should be in the database!");
        }

        ArrayList<Person> retrievedPersons = new Person().search(db);
        assertEquals(retrievedPersons.size(), 2);

        Person p1 = retrievedPersons.get(0);
        Person p2 = retrievedPersons.get(1);

        assertEquals(p1, me);
        assertEquals(p1.getPetList(), me.getPetList());
        assertEquals(p1.getOtherList(), me.getOtherList());
        assertEquals(p2, you);
        assertEquals(p2.getX(), you.getX());


        /*
         * Check if update works....
         */
        p1.getPetList().add(dog);
        try {
            p1.update(db);
            retrievedPersons = new Person().search(db);
            p1 = retrievedPersons.get(0);
            assertTrue(p1.getPetList().contains(dog));
        } catch (NoUniqueFieldException ex) {
            Logger.getLogger(Many2ManyTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(Many2ManyTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        lock.unlock();
    }
    

    @Test
    public void addingNullCollection() throws DuplicateKeyException {

        lock.lock();

        new Person().delete(db);
        new Pet().delete(db);

        Person me = new Person("my message", null, null);

        try {
            me.register(db);
        } catch (Throwable ex) {
            if (ex instanceof ImproperRegistration) {
                lock.unlock();
                return;
            }
        }
        lock.unlock();
        fail("SHOULD HAVE FAILED!");
    }

    @Test
    public void testSpecialSearch() throws ImproperRegistration{
        lock.lock();
        new Pet().delete(db);
        new Person().delete(db);
        Pet dog = new Pet("Jack", "black");
        Pet cat = new Pet("Juan", "white");
        dog.attemptRegister(db);
        cat.attemptRegister(db);

        Pet prototype = new Pet();
        prototype.setColor("%a%");
        ArrayList<Pet> searchedPets = prototype.search(db);
        assertEquals(searchedPets.size(), 1);
        assertEquals(dog, searchedPets.get(0));
        assertEquals(dog.getName(), searchedPets.get(0).getName());
        assertEquals(dog.getColor(), searchedPets.get(0).getColor());

        Collection<Pet> catDog = new ArrayList<Pet>();
        catDog.add(cat);
        catDog.add(dog);
        Person jenny = new Person("xyz", catDog, catDog);
        jenny.attemptRegister(db);

        catDog.remove(dog);
        jenny = new Person("abc", catDog, catDog);
        jenny.attemptRegister(db);

        Person prot = new Person();
        prot.setPetList(catDog);
        assertEquals(prot.search(db).get(0).getPetList().size(),2);
        assertEquals(prot.search(db).get(1).getPetList().size(),1);
        lock.unlock();
    }

    //@Test
    public void doItAgain() throws ImproperRegistration {
        testManyToMany();
        testManyToMany();
        testSpecialSearch();
    }
}
