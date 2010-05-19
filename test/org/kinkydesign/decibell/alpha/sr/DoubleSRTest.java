package org.kinkydesign.decibell.alpha.sr;

import java.util.ArrayList;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;

public class DoubleSRTest {

    @Test
    public void testSelfRef() throws Exception {
        DeciBell db = new DeciBell();
        db.setDbName("my/dvb/doublesr1");
        db.attach(DoubleSelfRef.class);
        db.setVerbose(true);
        db.start();

        DoubleSelfRef dsr1 = new DoubleSelfRef("x", "y", "z");
        dsr1.setFriend(dsr1);
        dsr1.attemptRegister(db);


        DoubleSelfRef dsr2 = new DoubleSelfRef("x", "y", "z");
        dsr2.setFriend(dsr1);
        dsr2.attemptRegister(db);

        System.out.println(new DoubleSelfRef().search(db));
    }
}
