/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.testcases.db102x;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author chung
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value={
    org.kinkydesign.decibell.testcases.db102x.Wrong4Test.class,
    org.kinkydesign.decibell.testcases.db102x.Wrong3Test.class,
    org.kinkydesign.decibell.testcases.db102x.Wrong2Test.class,
    org.kinkydesign.decibell.testcases.db102x.Wrong1Test.class} )

public class Db102xSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
