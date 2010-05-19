package org.kinkydesign.decibell.alpha.sr;

import java.util.ArrayList;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;

public class SRTest {

    @Test
    public void testSelfRef() throws Exception {
        DeciBell db = new DeciBell();
        db.setDbName("my/dvb/selfref142a");
        db.attach(SelfRef.class);
        db.setVerbose(true);
        db.start();

        SelfRef sr1 = new SelfRef(412, "x", "y", "z");
        sr1.setFriend(sr1);
        sr1.setLongValue(5231L);

        SelfRef sr2 = new SelfRef(908, null, null, null);
        sr2.setFriend(sr1);
        sr2.setLongValue(1234L);

        sr1.attemptRegister(db);
        assertEquals(1, sr1.attemptRegister(db));
        sr2.attemptRegister(db);
        assertEquals(1, sr2.attemptRegister(db));

        SelfRef prototype = new SelfRef();
        ArrayList<SelfRef> foundInDB = prototype.search(db);
        assertNotNull(foundInDB);
        assertEquals(2, foundInDB.size());
        assertTrue(foundInDB.contains(sr1));
        assertTrue(foundInDB.contains(sr2));

        SelfRef prototype2 = new SelfRef(-1, "x", null, null);
        ArrayList<SelfRef> foundInDB2 = prototype2.search(db);
        assertNotNull(foundInDB2);
        assertEquals(1, foundInDB2.size());
        assertTrue(foundInDB.contains(sr1));
        assertFalse(foundInDB2.contains(sr2));
        assertNotNull(foundInDB2.get(0).getFriend().getFriend().getFriend().getFriend());
        assertEquals(sr1, foundInDB2.get(0).getFriend());
        assertEquals(sr1.getA(), foundInDB2.get(0).getFriend().getA());


        SelfRef prototype3 = new SelfRef(-1, null, null, null);
        prototype3.setLongValue(1234L);
        ArrayList<SelfRef> foundInDB3 = prototype3.search(db);
        assertNotNull(foundInDB3);
        assertEquals(1, foundInDB3.size());
        assertTrue(foundInDB3.contains(sr2));
        assertFalse(foundInDB3.contains(sr1));
        assertNotNull(foundInDB3.get(0).getA());
        assertEquals("A-DEFAULT", foundInDB3.get(0).getA());
        assertEquals("B-DEFAULT", foundInDB3.get(0).getB());
        assertEquals("C_DEFAULT", foundInDB3.get(0).getC());
        assertEquals(sr1, foundInDB3.get(0).getFriend());

    }
}
