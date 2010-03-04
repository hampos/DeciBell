/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kinkyDesign.decibell.exceptions.NoUniqueFieldException;

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
        user.delete();
    }

}