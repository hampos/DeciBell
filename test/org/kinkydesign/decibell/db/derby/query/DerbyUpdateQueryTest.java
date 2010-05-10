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
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.collections.SQLType;
import org.kinkydesign.decibell.db.TableColumn;
import org.kinkydesign.decibell.db.derby.DerbyTable;
import org.kinkydesign.decibell.db.interfaces.JTable;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class DerbyUpdateQueryTest {

    public DerbyUpdateQueryTest() {
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
    public void testSomeMethod() {
        JTable t3 = new DerbyTable();
        t3.setTableName("itsme","C");
        TableColumn c1 = new TableColumn("f");
        c1.setColumnType(SQLType.INTEGER);
        c1.setPrimaryKey(true);
        t3.addColumn(c1);
        TableColumn c2 = new TableColumn("k");
        c2.setColumnType(SQLType.VARCHAR);
        c2.setPrimaryKey(true);
        t3.addColumn(c2);


        JTable t2 = new DerbyTable();
        t2.setTableName("itsme","B");
        TableColumn b1 = new TableColumn("i");
        b1.setForeignKey(t3, c1, OnModification.CASCADE, OnModification.NO_ACTION);
        b1.setColumnType(SQLType.INTEGER);
        b1.setPrimaryKey(true);
        t2.addColumn(b1);
        TableColumn b2 = new TableColumn("j");
        b2.setColumnType(SQLType.VARCHAR);
        t2.addColumn(b2);

        JTable t = new DerbyTable();
        t.setTableName("itsme","A");

        TableColumn tc1 = new TableColumn("x");
        tc1.setColumnType(SQLType.INTEGER);
        tc1.setForeignKey(t2, b1, OnModification.CASCADE, OnModification.NO_ACTION);
        t.addColumn(tc1);

        TableColumn tc2 = new TableColumn("y");
        tc2.setColumnType(SQLType.VARCHAR);
        tc2.setForeignKey(t2, b1, OnModification.CASCADE, OnModification.NO_ACTION);
        t.addColumn(tc2);

        TableColumn tc3 = new TableColumn("z");
        tc3.setColumnType(SQLType.INTEGER);
        tc3.setForeignKey(t3, c1, OnModification.CASCADE, OnModification.CASCADE);
        tc3.setPrimaryKey(true);
        t.addColumn(tc3);

        TableColumn tc4 = new TableColumn("w");
        tc4.setColumnType(SQLType.INTEGER);
        tc4.setForeignKey(t3, c2, OnModification.CASCADE, OnModification.CASCADE);
        tc4.setPrimaryKey(true);
        t.addColumn(tc4);

        DerbyUpdateQuery DU = new DerbyUpdateQuery(t);
        System.out.println(DU.getSQL());
    }
}
