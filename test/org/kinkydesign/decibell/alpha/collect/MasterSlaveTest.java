package org.kinkydesign.decibell.alpha.collect;

import java.util.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

public class MasterSlaveTest {

    private static DeciBell db = new DeciBell();
    private static boolean firstTestFinished = false;
    private static boolean secondTestFinished = false;

    @BeforeClass
    public static void beforeClass() throws Exception {
        db.setDbName("my/dvb/jfufes92");
        db.attach(Master.class);
        db.attach(Slave.class);
        db.start();
    }

    @AfterClass
    public static void afterClass() {
        db.reset();
        db.stop();
    }

    @Test
    public void testMasterSlave() throws ImproperRegistration {

        try {
            new Master().delete(db);

            Slave s1 = new Slave(724, "nfus", "foesif");
            Slave s2 = new Slave(101, "83d", "bden");
            Slave s3 = new Slave(1234, "okf904if", "ogd");
            Slave s4 = new Slave(999, "fsuh", "--e");
            s1.attemptRegister(db);
            s2.attemptRegister(db);
            s3.attemptRegister(db);
            s4.attemptRegister(db);

            List<Slave> myList = new ArrayList<Slave>();
            myList.add(s1);
            myList.add(s2);
            myList.add(s3);
            myList.add(s4);

            Master m1 = new Master("master1", myList);
            m1.attemptRegister(db);
            assertEquals(1, m1.attemptRegister(db));

            List<Slave> otherList = new ArrayList<Slave>();
            otherList.add(s1);
            otherList.add(s2);

            Master m2 = new Master("master2", otherList);
            m2.attemptRegister(db);
            assertEquals(1, m2.attemptRegister(db));

            Master m3 = new Master("master3", otherList);
            m3.attemptRegister(db);
            assertEquals(1, m3.attemptRegister(db));

            //System.out.println(m3);

            ArrayList<Master> mastersFound = new Master().search(db);
            assertNotNull(mastersFound);
            assertEquals(3, mastersFound.size());
            assertTrue(mastersFound.contains(m1));
            assertTrue(mastersFound.contains(m2));
            assertTrue(mastersFound.contains(m3));

            ArrayList<Master> searchForM1 = new Master("%1", null).search(db);
            assertNotNull(searchForM1);
            assertEquals(1, searchForM1.size());
            assertTrue(searchForM1.contains(m1));
            assertEquals(m1.getId(), searchForM1.get(0).getId());
            Master retrievedM1 = searchForM1.get(0);
            assertEquals(4, retrievedM1.getSlaves().size());
            assertTrue(retrievedM1.getSlaves().contains(s1));
            assertTrue(retrievedM1.getSlaves().contains(s2));
            assertTrue(retrievedM1.getSlaves().contains(s3));
            assertTrue(retrievedM1.getSlaves().contains(s4));
            assertTrue(retrievedM1.getSlaves() instanceof ArrayList);

            ArrayList<Master> searchForM2 = new Master("%2", null).search(db);
            assertNotNull(searchForM2);
            assertEquals(1, searchForM2.size());
            assertEquals(m2.getId(), searchForM2.get(0).getId());
            assertTrue(searchForM2.get(0).getSlaves() instanceof ArrayList);
            assertEquals(2, searchForM2.get(0).getSlaves().size());
            assertTrue(searchForM2.get(0).getSlaves().contains(s1));
            assertTrue(searchForM2.get(0).getSlaves().contains(s2));

        } finally {
            firstTestFinished = true;
        }
    }

    @Test
    public void testListType() throws InterruptedException, ImproperRegistration {
        while (!firstTestFinished) {
            Thread.sleep(250);
        }
        try {
            Slave s1 = new Slave(724, "nfus", null);
            Slave s2 = new Slave(101, "83d", null);
            Slave s3 = new Slave(1234, "okf904if", null);

            List<Slave> list = new LinkedList<Slave>();
            list.add(s1);
            list.add(s2);
            list.add(s3);
            Master master_1 = new Master("linked master", list);

            master_1.attemptRegister(db);
            assertEquals(1, master_1.attemptRegister(db));

            ArrayList<Master> retrievedMast1 = new Master("linked%", null).search(db);
            assertNotNull(retrievedMast1);
            assertEquals(1, retrievedMast1.size());
            assertTrue(retrievedMast1.contains(master_1));
            assertTrue(retrievedMast1.get(0).getSlaves() instanceof LinkedList);
            assertEquals(3, retrievedMast1.get(0).getSlaves().size());
            assertTrue(retrievedMast1.get(0).getSlaves().contains(s1));
            assertTrue(retrievedMast1.get(0).getSlaves().contains(s2));
            assertTrue(retrievedMast1.get(0).getSlaves().contains(s3));

            List<Slave> mL = new Vector<Slave>();
            mL.add(s1);
            Master mst = new Master("vector master", mL);
            mst.attemptRegister(db);

            ArrayList<Master> retrievedVectorMaster = new Master("%ector%", null).search(db);
            assertNotNull(retrievedVectorMaster);
            assertEquals(1, retrievedVectorMaster.size());
            assertTrue(retrievedVectorMaster.contains(mst));
            assertTrue(retrievedVectorMaster.get(0).getSlaves() instanceof Vector);

        } finally {
            secondTestFinished = true;
        }
    }

    @Test
    public void emptyCollection() throws InterruptedException, ImproperRegistration {
        while (!secondTestFinished) {
            Thread.sleep(250);
        }
        Master havingEmptyCollection = new Master("empty master", new Stack<Slave>());
        havingEmptyCollection.attemptRegister(db);
        assertEquals(1, havingEmptyCollection.attemptRegister(db));

        Master retrievedMaster = new Master("empty%", null).search(db).get(0);
        assertEquals(havingEmptyCollection, retrievedMaster);
        assertTrue(retrievedMaster.getSlaves().isEmpty());
        assertTrue(retrievedMaster.getSlaves() instanceof List);
        assertFalse(retrievedMaster.getSlaves() instanceof ArrayList);
    }
}
