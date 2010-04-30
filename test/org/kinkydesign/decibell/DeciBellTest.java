package org.kinkydesign.decibell;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.examples.tutorial.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    public void testStart() throws FileNotFoundException, DuplicateKeyException {
        System.out.println("start");
        DeciBell db = new DeciBell();

        db.setDriverHome(System.getenv("DERBY_HOME"));
        db.setDbName("database/new/14");

        db.attach(SubSubUser.class);
        db.attach(UserGroup.class);
        db.attach(SubUser.class);
        db.attach(User.class);

        db.start();

        UserGroup ug = new UserGroup(309, "groupb");

        //ug.register(db);

        User user = new User();
        user.setId(123241);
        user.setUserName("Maria5");
        ArrayList<UserGroup> groupList = new ArrayList<UserGroup>();

        groupList.add(ug);
        user.setGroups(groupList);

        user.setGroup(ug);

        List<Resource> childName = new LinkedList<Resource>();
        childName.add(new Pool(15, "child1"));
        childName.add(new Pool(16, "child3"));

        user.setChildName(childName);

        user.setFriend(user);

    //    user.register(db);
        // user.delete(db);

        // user.delete(db);

        user.setAge(17);
        //  user.update(db);


        //PrintStream ps = new PrintStream(new File("/home/chung/Desktop/component.txt"));
        User v = new User();
        v.setId(-314);

        ArrayList<User> list = v.search(db);
        System.out.println("List size : " + list.size());
        for (User u : list) {
            u.print(System.out);
            System.out.println(u.getGroups().getClass());
        }

    }
}
