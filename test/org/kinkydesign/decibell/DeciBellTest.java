package org.kinkydesign.decibell;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.examples.tutorial.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        db.setDbName("database/nenn224");

        db.attach(SubSubUser.class);
        db.attach(UserGroup.class);
        db.attach(SubUser.class);
        db.attach(User.class);

        db.start();

        UserGroup ug = new UserGroup(309, "groupb");
        UserGroup ug2 = new UserGroup(245, "groupc");

     //   ug.register(db);

        User user = new User();
        user.setId(666);
        user.setUserName("Satan");
        Set<UserGroup> groupList = new LinkedHashSet<UserGroup>();

        groupList.add(ug);
     //   groupList.add(ug2);
        user.setGroups(groupList);

        user.setGroup(ug);

        List<Resource> childName = new LinkedList<Resource>();
        childName.add(new Pool(15, "child1"));
        childName.add(new Pool(16, "child3"));
        childName.add(new Pool(17, "child4"));

        user.setChildName(childName);

        user.setFriend(user);

        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("MITSOS", new Integer(234));
        map.put("KITSOS", new Integer(233));
        user.setMap(map);
        
   //     user.register(db);
        // user.delete(db);

        // user.delete(db);

        user.setAge(2364);
     //     user.update(db);


        //PrintStream ps = new PrintStream(new File("/home/chung/Desktop/component.txt"));
        User v = new User();
        v.setId(-314);
        v.setMap(map);

        ArrayList<User> list = v.search(db);
        System.out.println("List size : " + list.size());
        for (User u : list) {
            u.print(System.out);
            System.out.println(u.getGroups().getClass());
        }

    }
}
