/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.testcases.db100x;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.db.engine.SearchEngine;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 *
 * @author chung
 */
public class ATest {

    private static DeciBell db;
    private static final Lock lock = new ReentrantLock();

    public ATest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.setDbName("testDB/db100x/xssfs");

        db.attach(A.class);
        db.attach(B.class);
        db.attach(C.class);
        db.attach(D.class);
        db.attach(E.class);

        lock.lock();
        db.start();
        System.out.println("CONNECT '" + db.getDatabaseUrl() + "';");
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
    public void testdb100x() throws ImproperRegistration {

        E e1 = new E("e1");
        E e2 = new E("e2");
        E e3 = new E("e3");
        ArrayList<E> eList = new ArrayList<E>();

        eList.add(e1);
        eList.add(e2);
        eList.add(e3);
        D d = new D(154);
        C c1 = new C("c1");
        C c2 = new C("c2");
        C c3 = new C("c3");
        C c4 = new C("c4");
        ArrayList<C> cList = new ArrayList<C>();
        cList.add(c1);
        cList.add(c2);
        cList.add(c3);
        cList.add(c4);
        B b = new B("b new");
        b.setD(d);
        b.setcList(cList);

        System.out.print(e1.attemptRegister(db) + " , ");
        System.out.print(e2.attemptRegister(db) + " , ");
        System.out.print(e3.attemptRegister(db) + " , ");
        System.out.print(d.attemptRegister(db) + " , ");
        System.out.print(c1.attemptRegister(db) + " , ");
        System.out.print(c2.attemptRegister(db) + " , ");
        System.out.print(c3.attemptRegister(db) + " , ");
        System.out.print(c4.attemptRegister(db) + " , ");
        System.out.print(b.attemptRegister(db) + " , ");

        A a1 = new A("a new 1");
        a1.setB(b);
        a1.seteList(eList);
        a1.attemptRegister(db);

        A a2 = new A("a my a");
        a2.setB(b);
        eList.remove(e1);
        a2.seteList(eList);

        try {
            System.out.print(a1.attemptRegister(db) + " , ");
            System.out.print(a2.attemptRegister(db) + " .\n");
        } catch (ImproperRegistration ex) {
            fail("IMPROPER REGISTRATION!");
        }

        new A().find(db).get(0).print(System.out);
        new A().find(db).get(1).print(System.out);


        B subPrototype = new B();
        subPrototype.setD(new D(154));
        A prototype = new A();
        prototype.setB(subPrototype);

        assertEquals(2, new SearchEngine<A>(db).find(prototype).size());

        subPrototype = new B();
        subPrototype.setD(new D(15353));
        prototype = new A();
        prototype.setB(subPrototype);

        assertEquals(0, new SearchEngine<A>(db).find(prototype).size());

        assertEquals(1, new B("b new").find(db).size());
        assertEquals(0, new B("sadf").find(db).size());


    }
}
