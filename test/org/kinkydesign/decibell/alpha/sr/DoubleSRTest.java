package org.kinkydesign.decibell.alpha.sr;

import java.util.ArrayList;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;

public class DoubleSRTest {

    @Test
    public void testSelfRef() throws Exception {
        DeciBell db = new DeciBell();
        db.setDbName("my/dvb/doublesr2");
        db.attach(DoubleSelfRef.class);
        db.setVerbose(true);
        db.start();

        DoubleSelfRef dsr1 = new DoubleSelfRef("x", "y", "z");
        dsr1.setFriend(dsr1);
        dsr1.attemptRegister(db);
        assertEquals(1, dsr1.attemptRegister(db));


        DoubleSelfRef dsr2 = new DoubleSelfRef("r", "s", "t");
        dsr2.setFriend(dsr1);
        dsr2.attemptRegister(db);
        assertEquals(1, dsr2.attemptRegister(db));

        ArrayList<DoubleSelfRef> foundInDB = new DoubleSelfRef().search(db);
        assertNotNull(foundInDB);
        assertEquals(2, foundInDB.size());
        assertTrue(foundInDB.contains(dsr1));
        assertTrue(foundInDB.contains(dsr2));


        DoubleSelfRef prototype = new DoubleSelfRef("r", null, null);
        ArrayList<DoubleSelfRef> foundInDB2 = prototype.search(db);
        assertNotNull(foundInDB2);
        assertEquals(1, foundInDB2.size());
        assertTrue(foundInDB2.contains(dsr2));
        assertFalse(foundInDB2.contains(dsr1));
        assertEquals(dsr1,foundInDB2.get(0).getFriend());
        assertEquals(dsr1,foundInDB2.get(0).getFriend().getFriend());

    }
}
