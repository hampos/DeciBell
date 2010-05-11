/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kinkydesign.decibell.testcases.db105x;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DeciBellException;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author chung
 */
public class NullCaseTest {

    public NullCaseTest() {
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
    public void testSomeMethod() throws DeciBellException {
        DeciBell db = new DeciBell();
        db.setDbName("decibellTestDB/nullcase/nc2");

        db.attach(NullCase.class);
        db.start();

        NullCase nc = new NullCase(null, null);
        nc.delete(db);

        nc.attemptRegister(db);
        assertEquals(nc.attemptRegister(db), 1);

        NullCase retrievedCase = nc.search(db).get(0);
        assertNotNull(retrievedCase);
        assertEquals(retrievedCase.getMyEntry(),"__NULL__");
        assertEquals(retrievedCase.getUid(),"__NULL__");

        nc.delete(db);
        assertEquals(nc.search(db).size(),0);
        nc.attemptRegister(db);
        nc = new NullCase("__NULL__", "message");
        nc.update(db);

        retrievedCase = nc.search(db).get(0);
        assertNotNull(retrievedCase);
        assertEquals(retrievedCase.getMyEntry(), nc.getMyEntry());
        assertEquals(retrievedCase.getUid(), nc.getUid());
    }

}