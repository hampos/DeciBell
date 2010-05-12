/**
 *  Class : YAQPTest
 *  Date  : May 1, 2010
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

package org.kinkydesign.decibell.examples.yaqp;

import java.net.URI;
import java.net.URISyntaxException;
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

/**
 *
 * @author chung
 */
public class YAQPTest {

    private static DeciBell db = new DeciBell();
    private static final Lock lock = new ReentrantLock();

    public YAQPTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        db.setDbName("decibellTestDB/yaqp/adhsyf");

        db.attach(ErrorReport.class);
        db.attach(ErrorCode.class);
        db.attach(Task.class);
        db.start();
        System.out.println("YAQP-test DB initialized");
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
    public void testYaqpOperations() throws URISyntaxException, DuplicateKeyException, ImproperRegistration {
        ErrorCode ec = new ErrorCode();
        ec.setCode(12342);
        ec.setHttpStatus(500);
        ec.setMessage("unknown error");

        ErrorReport er = new ErrorReport();
        er.setUid(7);
        er.setErrorCode(ec);
        er.setTrace(er);
        er.setActor("none");
        er.setDetails("details");

        lock.lock();
        new Task().delete(db);
        new ErrorReport().delete(db);
        new ErrorCode().delete(db);

        /**
         * Verify that there is nothing in the database
         */
        assertEquals(
                new ErrorCode().search(db).size()
                + new ErrorReport().search(db).size()
                + new Task().search(db).size(), 0);


        try {
            ec.register(db);
            System.out.println("[SUCCESS] Error code successfully registered!");
        } catch (DuplicateKeyException ex) {
            System.err.println("[WARNING] DuplicateKey Exception for the error code object!?");
        }


        try {
            er.register(db);
            System.out.println("[SUCCESS] Error report successfully registered!");
        } catch (DuplicateKeyException ex) {
            System.err.println("[WARNING] DuplicateKey Exception for the error "
                    + "report object!?");
        }

        ArrayList<ErrorReport> retrievedReports = new ErrorReport().search(db);
        assertNotNull("[FAIL] Failed to resolve self-references",
                retrievedReports.iterator().next().getTrace().getTrace().getTrace());
        assertEquals(retrievedReports.iterator().next().getTrace().getTrace().getTrace().getErrorCode().getMessage(), ec.getMessage());

        lock.unlock();
    }

    @Test
    public void YAQPTaskTest() throws URISyntaxException, DuplicateKeyException, ImproperRegistration {

        ErrorReport er = new ErrorReport();
        er.setUid(7); // <== this is already in the database!

        /*
         * Delete all tasks from the database...
         */
        new Task().delete(db);
        assertEquals(new Task().search(db).size(), 0); // there should now be no tasks in the database

        Task task = new Task();
        task.setEr(er);
        task.setDurationMS(1002);
        task.setResultURI(new URI("http://something.com"));
        task.setTaskStatus(143);
        task.setTimeStart(15);
        task.setTimeFinish(998);
        task.setUid(555);

        lock.lock();
            task.register(db);
            Task retrievedTask = task.search(db).iterator().next();
            assertNotNull(retrievedTask.getEr().getTrace().getTrace().getErrorCode());
            assertEquals(retrievedTask.getEr().getErrorCode().getHttpStatus(), 500);
            task.search(db).iterator().next().print(System.out);
        lock.unlock();
    }

    @Test
    public void doItAgain() throws URISyntaxException, DuplicateKeyException, ImproperRegistration{
        lock.lock();
        YAQPTaskTest();
        testYaqpOperations();
        lock.unlock();
    }
}
