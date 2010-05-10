/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kinkydesign.decibell.db.derby.query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.derby.DerbyTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.query.InsertQuery;

/**
 *
 * @author chung
 */
public class DerbyInsertQueryTest {

    public DerbyInsertQueryTest() {
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
    public void testGetSQL() {
        JTable t = new DerbyTable();
        t.setTableName("itsme","MY_TABLE");

        TableColumn tc1 = new TableColumn("A");
        tc1.setColumnType(SQLType.INTEGER);
        t.addColumn(tc1);

        TableColumn tc2 = new TableColumn("B");
        tc2.setColumnType(SQLType.INTEGER);
        t.addColumn(tc2);

        InsertQuery a = new DerbyInsertQuery(t);
        System.out.println(a.getSQL());

    }

}