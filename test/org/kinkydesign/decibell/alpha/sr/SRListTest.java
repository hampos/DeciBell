package org.kinkydesign.decibell.alpha.sr;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;

public class SRListTest {

    @Test
    public void testSRList() throws Exception {
        DeciBell db = new DeciBell();
        db.setDbName("my/dvb/7ejf");
        db.attach(SRList.class);
        db.setVerbose(true);
        db.start();

        SRList a = new SRList();

        a.setId(UUID.randomUUID().toString());
        ArrayList<SRList> myList = new ArrayList<SRList>();
        myList.add(a);

        a.setMyList(new ArrayList<SRList>());
//        a.register(db);

        ArrayList<SRList> found = new SRList("%", null).search(db);
        for (SRList sr : found) {
            try {
                System.out.println(sr.getMyList().get(0).getMyList().get(0).getId());
                System.out.println(sr);
            } catch (IndexOutOfBoundsException ex) {
            }
        }

    }
}
