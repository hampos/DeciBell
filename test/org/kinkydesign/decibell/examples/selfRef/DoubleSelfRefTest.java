package org.kinkydesign.decibell.examples.selfRef;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;

/**
 *
 * @author chung
 */
public class DoubleSelfRefTest {

    private static DeciBell db;

    public DoubleSelfRefTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = new DeciBell();
        db.attach(DoubleSelfRef.class);
        db.setDbName("decibellTestDB/subclassing/e012avav");
        db.start();
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
    public void testSomeMethod() {

        new DoubleSelfRef().delete(db);

        DoubleSelfRef dsr = new DoubleSelfRef(14, "mlka");
        dsr.setMe(dsr);
        dsr.setMy(dsr);
        dsr.setMe(dsr);
        dsr.setMy(dsr);
        try {
            dsr.register(db);
        } catch (DuplicateKeyException ex) {
            fail();
        } catch (ImproperRegistration ex) {
            fail();
        }

        assertEquals(new DoubleSelfRef().search(db).iterator().next().getMe(), dsr);
        assertEquals(new DoubleSelfRef().search(db).iterator().next().getMy(), dsr);

    }
}
