/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.Component;
import static org.junit.Assert.*;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 *
 * @author hampos
 */
public class UserTest {

    public UserTest() {
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
     * Test of myMethod method, of class User.
     */
    @Test
    public void testMyMethod() {
    }

    /**
     * Test of setUseName method, of class User.
     */
    @Test
    public void testSetUseName() {
    }

    @Test
    public void testClass() throws NoUniqueFieldException{
        User user = new User();
        ArrayList<User> list = user.search();
    //    System.out.println(list.get(0).id);
      //  user.delete();
        DeciBell db = new DeciBell();
        db.start();
    }

}