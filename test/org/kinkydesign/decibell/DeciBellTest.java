package org.kinkydesign.decibell;

import examples.SubSubUser;
import examples.SubUser;
import examples.User;
import examples.UserGroup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;

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
    public void testStart() throws DuplicateKeyException {
        System.out.println("start");
        DeciBell db = new DeciBell();

        db.setDriverHome("/Applications/NetBeans/sges-v3/javadb");
        db.setDbName("chamo4");

        db.attach(SubSubUser.class);
        db.attach(UserGroup.class);
        db.attach(SubUser.class);
        db.attach(User.class);

        
        db.start();

        UserGroup group = new UserGroup(1,"admin");
        User user = new User();
        user.setId(5);
        user.setUserName("hampos");
        user.setAge(25);
        user.setGroup(group);

     //   group.register(db);
        System.out.println("Group passed");
     //   user.register(db);

     //   user.delete(db);
        User u = new User();
        u.setId(5);
    //    u.setAge(25);
        u.delete(db);

        

     //   db.reset();

     //   db.stop();


    }

}