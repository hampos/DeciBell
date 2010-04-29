package org.kinkydesign.decibell;

import org.kinkydesign.decibell.examples.tutorial.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
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
    public void testStart() throws DuplicateKeyException, FileNotFoundException {
        System.out.println("start");
        DeciBell db = new DeciBell();

        db.setDriverHome("/home/chung/JLib/10.6.0.0alpha_2010-02-15T19-30-14_SVN910262");
        db.setDbName("database/new/6");

        db.attach(SubSubUser.class);
        db.attach(UserGroup.class);
        db.attach(SubUser.class);
        db.attach(User.class);

        db.start();

        UserGroup ug = new UserGroup(989, "groupa");
        //ug.register(db);

        User user = new User();
        user.setId(202232);
        user.setUserName("Maria1");
        ArrayList<UserGroup> groupList = new ArrayList<UserGroup>();
        UserGroup group = new UserGroup(1, "secta");
        //groupList.add(group);
        groupList.add(ug);
        user.setGroups(groupList);
        user.setGroup(ug);

         user.register(db);
        //user.delete(db);


        //PrintStream ps = new PrintStream(new File("/home/chung/Desktop/component.txt"));
        ArrayList<User> list = new User().search(db);
        System.out.println("List size : " + list.size());
        for (User u : list) {
            u.print(System.out);
        }

    }
}
