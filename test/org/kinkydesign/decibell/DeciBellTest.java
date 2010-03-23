package org.kinkydesign.decibell;

import examples.SubUser;
import examples.User;
import examples.UserGroup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author revan
 */
public class DeciBellTest {

    public DeciBellTest() {
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

    /**
     * Test of start method, of class DeciBell.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        DeciBell instance = new DeciBell();
        instance.attach(UserGroup.class);
        instance.attach(SubUser.class);
        instance.attach(User.class);
        
        instance.start();


    }

}