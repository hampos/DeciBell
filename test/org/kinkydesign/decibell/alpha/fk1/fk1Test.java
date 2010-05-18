package org.kinkydesign.decibell.alpha.fk1;

import java.util.ArrayList;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;

public class fk1Test {

    public fk1Test() {
    }

    

    @Test
    public void test1() throws Exception {

        DeciBell db = new DeciBell();
        db.setDbName("my/dvb/xxysaf2");
        db.attach(A.class);
        db.attach(B.class);
        db.attach(C.class);
        db.attach(D.class);
        db.start();

        D d1 = new D("d1", "d1ddd", 12);
        D d2 = new D("d2", "d2ddd-ccc", 54);
        D d3 = new D("d44", "ccds", -43);
        D d4 = new D("d44", "ccds2", -43);
        D d5 = new D("d1", "something", -43);

        d1.attemptRegister(db);
        assertEquals(1, d1.attemptRegister(db));
        d2.attemptRegister(db);
        assertEquals(1, d2.attemptRegister(db));
        d3.attemptRegister(db);
        assertEquals(1, d3.attemptRegister(db));
        d4.attemptRegister(db);
        assertEquals(1, d4.attemptRegister(db));
        d5.attemptRegister(db);
        assertEquals(1, d5.attemptRegister(db));

        C c1 = new C(d1, "c1--1", "chdsferfgs");
        C c2 = new C(d1, "c1--2", "824rnf");
        C c3 = new C(d3, "c1--3", "orgnkj");
        C c4 = new C(d2, "c1--4", "urghin");

        c1.attemptRegister(db);
        c2.attemptRegister(db);
        c3.attemptRegister(db);
        c4.attemptRegister(db);

        B b1 = new B("b1-a", "b1-b");
        B b2 = new B("b2-a", "b3-b");
        B b3 = new B("b3-a", "b4-b");

        b1.attemptRegister(db);
        b2.attemptRegister(db);
        b3.attemptRegister(db);

        A a1 = new A("a1-key", b3, c4);
        A a2 = new A("a2-key", b1, c2);
        A a3 = new A("a3-key", b3, c3);

        a1.attemptRegister(db);
        assertEquals(1, a1.attemptRegister(db));
        a2.attemptRegister(db);
        assertEquals(1, a2.attemptRegister(db));
        a3.attemptRegister(db);
        assertEquals(1, a3.attemptRegister(db));


        ArrayList<A> listOfA = new A().search(db);
        assertTrue(listOfA.contains(a1));
        assertTrue(listOfA.contains(a2));
        assertTrue(listOfA.contains(a3));

        ArrayList<A> listOfA2 = new A("a1%", null, null).search(db);
        assertEquals(3, listOfA.size());
        assertTrue(listOfA2.contains(a1));
        assertFalse(listOfA2.contains(a2));
        assertFalse(listOfA2.contains(a3));

        assertEquals(1, listOfA2.size());
        assertEquals(a1.getaKey(), listOfA2.get(0).getaKey());
        assertEquals(a1.getcLink(), listOfA2.get(0).getcLink());
        assertEquals(a1.getbLink(), listOfA2.get(0).getbLink());

        ArrayList<D> dList = new D().search(db);
        assertTrue(dList.contains(d1));
        assertTrue(dList.contains(d2));
        assertTrue(dList.contains(d3));
        assertTrue(dList.contains(d4));
        assertTrue(dList.contains(d5));
        assertEquals(5, dList.size());


        ArrayList<D> dList2 = d4.search(db);
        assertTrue(dList2.contains(d4));
        assertEquals(1, dList2.size());

        ArrayList<D> searchForSomeD = new D(null, "%s2", -1).search(db);
        assertEquals(1, searchForSomeD.size());
        assertTrue(searchForSomeD.contains(d4));

        ArrayList<C> noCfound = new C(null, null, "wtf").search(db);
        assertNotNull(noCfound);
        assertTrue(noCfound.isEmpty());

        new A().delete(db);
        listOfA = new A().search(db);
        assertNotNull(listOfA);
        assertEquals(0, listOfA.size());

    }
}
