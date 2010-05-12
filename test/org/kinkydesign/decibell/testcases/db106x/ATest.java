/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kinkydesign.decibell.testcases.db106x;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DeciBellException;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;

/**
 *
 * @author chung
 */
public class ATest {

    public ATest() {
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
//        db.attachFromPackages("org.kinkydesign.decibell.examples.db106x");
        db.attach(A.class);
        db.attach(B.class);
        db.attach(C.class);
        db.setDbName("testDB/test/a123");
        db.start();

        C c1 = new C();
        c1.c = "c1";
        c1.attemptRegister(db);

        C c2 = new C();
        c2.c = "c2";
        c2.attemptRegister(db);


        B b1 = new B();
        b1.b = "b1";
        b1.c = c1;
        b1.attemptRegister(db);

        B b2 = new B();
        b2.b = "b2";
        b2.c = c1;
        b2.attemptRegister(db);

        B b3 = new B();
        b3.b = "b3";
        b3.c = c2;
        b3.attemptRegister(db);

        B b4 = new B();
        b4.b = "b4";
        b4.c = c2;
        b4.attemptRegister(db);

        A a1 = new A();
        a1.a = "a1";
        a1.b = b1;
        a1.attemptRegister(db);

        A a2 = new A();
        a2.a = "a2";
        a2.b = b2;
        a2.attemptRegister(db);

        A a3 = new A();
        a3.a = "a3";
        a3.b = b3;
        a3.attemptRegister(db);

        A a4 = new A();
        a4.a = "a4";
        a4.b = b4;
        a4.attemptRegister(db);

        A a = new A();
        B b = new B();
        b.c = c2;
        a.b = b;

        assertEquals(2, a.search(db).size());
    }

}