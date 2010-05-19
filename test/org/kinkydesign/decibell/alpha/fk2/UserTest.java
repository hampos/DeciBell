package org.kinkydesign.decibell.alpha.fk2;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DeciBellException;
import static org.junit.Assert.*;

/**
 *
 * @author chung
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

    @Test
    public void testSomeMethod() throws DeciBellException {

        DeciBell db = new DeciBell();
        db.setDbName("my/dvb/15");
        db.attach(User.class);
        db.attach(UserInfo.class);
        db.attach(Task.class);
        db.start();

        Task t1 = new Task();
        t1.comment = "comment1";
        t1.id = "id1";
        t1.attemptRegister(db);
        assertEquals(1, t1.attemptRegister(db));

        Task t2 = new Task();
        t2.comment = "comment2";
        t2.id = "id2";
        t2.attemptRegister(db);
        assertEquals(1, t2.attemptRegister(db));

        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks.add(t1);
        tasks.add(t2);

        UserInfo ui = new UserInfo();
        ui.email = "mail1";
        ui.uname = "JS";
        ui.attemptRegister(db);
        assertEquals(1, ui.attemptRegister(db));

        User u = new User();
        u.delete(db);
        u.tasks = tasks;
        u.ui = ui;
        u.attemptRegister(db);
        assertEquals(1, u.attemptRegister(db));

        ArrayList<User> usersFound = new User().search(db);
        assertNotNull(usersFound);
        assertEquals(1, usersFound.size());
        assertEquals(u, usersFound.get(0));
        assertTrue(usersFound.get(0).tasks.contains(t1));
        assertTrue(usersFound.get(0).tasks.contains(t2));
        assertEquals(ui.email, usersFound.get(0).ui.email);
        assertEquals(ui.uname, usersFound.get(0).ui.uname);

    }

}