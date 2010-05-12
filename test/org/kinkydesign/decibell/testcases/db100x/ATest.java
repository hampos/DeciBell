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
        db.setDbName("testDB/db100x/x6xfs");

        db.attach(A.class);
        db.attach(B.class);
        db.attach(C.class);
        db.attach(D.class);
        db.attach(E.class);

        lock.lock();
        db.start();
        System.out.println("CONNECT '"+db.getDatabaseUrl()+"';");
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
    public void testdb100x() {

        E e1 = new E("e1");
        E e2 = new E("e2");
        E e3 = new E("e3");
        ArrayList<E> eList = new ArrayList<E>();
        eList.add(e1);eList.add(e2);eList.add(e3);
        D d = new D(154);
        C c1 = new C("c1");
        C c2 = new C("c2");
        C c3 = new C("c3");
        C c4 = new C("c4");
        ArrayList<C> cList = new ArrayList<C>();
        cList.add(c1);cList.add(c2);cList.add(c3);cList.add(c4);
        B b = new B("b new");
        b.setD(d);
        b.setcList(cList);
        
        A a1 = new A("a new 1");
        a1.setB(b);
        a1.seteList(eList);

       A a2 = new A("a my a");
        a2.setB(b);
        eList.remove(e1);
        a2.seteList(eList);

        try {
            System.out.print(e1.attemptRegister(db)+" , ");
            System.out.print(e2.attemptRegister(db)+" , ");
            System.out.print(e3.attemptRegister(db)+" , ");
            System.out.print(d.attemptRegister(db)+" , ");
            System.out.print(c1.attemptRegister(db)+" , ");
            System.out.print(c2.attemptRegister(db)+" , ");
            System.out.print(c3.attemptRegister(db)+" , ");
            System.out.print(c4.attemptRegister(db)+" , ");
            System.out.print(b.attemptRegister(db)+" , ");
            System.out.print(a1.attemptRegister(db)+" , ");
            System.out.print(a2.attemptRegister(db)+" .\n");
        } catch (ImproperRegistration ex) {
            fail("IMPROPER REGISTRATION!");
        }

        new A().search(db).iterator().next().print(System.out);
        new A().search(db).iterator().next().print(System.out);

        
        B subPrototype = new B();
        subPrototype.setD(new D(154));
        A prototype = new A();
        prototype.setB(subPrototype);
        System.out.println(prototype.search(db).size());

    }
}
