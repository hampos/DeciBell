/**
 *  Class : ModelTest
 *  Date  : May 10, 2010
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


package org.kinkydesign.decibell.testcases.db103x;

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

/**
 *
 * @author chung
 */
public class ModelTest {

    private static DeciBell db;
    private static final Lock lock = new ReentrantLock();

    public ModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("testDB/db103x/h88em881jd3v");

        db.attach(Model.class);
        db.attach(BibTex.class);

        db.start();
        System.out.println("CONNECT '"+db.getDatabaseUrl()+"';");
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
    public void testBibTex() throws DuplicateKeyException, ImproperRegistration {
        lock.lock();

        // Delete all BibTex items...
        new BibTex().delete(db);
        // Register some new stuff in the database...
        new BibTex("Jules Vern", "The mysterious island").register(db);
        new BibTex("Shakesphere", "Hamlet").register(db);
        new BibTex("Arkas", "The life prisoner").register(db);
        ArrayList<BibTex> retrievedBibTex = new BibTex().search(db);
        assertEquals(retrievedBibTex.size(), 3);

        ArrayList<BibTex> searchForIsland = new BibTex(null, "%island").search(db);
        assertEquals(searchForIsland.size(),1);
        assertEquals(searchForIsland.get(0).getAuthor(),"Jules Vern");

        ArrayList<BibTex> searchArkas = new BibTex("Arkas", null).search(db);
        assertEquals(searchArkas.get(0).getBook(),"The life prisoner");

        new BibTex("Arkas", null).delete(db);
        searchArkas = new BibTex("Arkas", null).search(db);
        assertEquals(searchArkas.size(),0);

        
        // Here is the way to update the book of Shackesphere:
        // 1. Retrieve the book from the database to get the Id (which we dont know yet)
        BibTex searchHamlet= new BibTex("Shake%", "Ha%").search(db).get(0);
        // 2. Make modifications
        searchHamlet.setBook("The Hamlet, volume 1");
        // 3. Update the book...
        searchHamlet.update(db);
        // 4. Check if is was done correctly:
        searchHamlet= new BibTex("Shake%", null).search(db).get(0);
        assertEquals(searchHamlet.getBook(),"The Hamlet, volume 1");
        assertEquals(searchHamlet.getAuthor(),"Shakesphere");
        lock.unlock();
    }

    @Test
    public void testModel() throws ImproperRegistration{
        lock.lock();
        BibTex anyBibTex = new BibTex().search(db).get(0);
        Model model = new Model(1542, "dataset1");
        model.setBibTex(anyBibTex);
        model.attemptRegister(db);

        anyBibTex = new BibTex().search(db).get(1);
        model = new Model(6341, "dataset2");
        model.setBibTex(anyBibTex);
        model.attemptRegister(db);

        assertEquals(new Model().search(db).size(),2);
        anyBibTex.delete(db); // deleting the bibtex should delete the model pointing to it
        assertEquals(new Model().search(db).size(),1);

        anyBibTex.attemptRegister(db);
        model.attemptRegister(db);
        assertEquals(new Model().search(db).size(),2);

        Model prot = new Model();
        prot.setBibTex(anyBibTex);
        prot.search(db).get(0).print(System.out);

        Model mod = new Model();
        mod.setBibTex(new BibTex("J%", null));
        mod.search(db).get(0).print(System.out);

        lock.unlock();
    }

}