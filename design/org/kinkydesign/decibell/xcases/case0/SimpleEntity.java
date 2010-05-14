package org.kinkydesign.decibell.xcases.case0;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;

public class SimpleEntity extends Component<SimpleEntity> {

    @PrimaryKey
    @Entry(defaultValue="DEFAULT PK VALUE")
    public String id;

    @Entry
    public String myentry;

    @Entry(defaultValue="HAHAHAHA!")
    public String otherEntry;

    @Entry
    @NumericNull("-1")
    public int myint;

    @Entry(defaultValue="554")
    @NumericNull("-1")
    public long mylong;

    public static void main(String... args) throws Exception{
        DeciBell db = new DeciBell();
        db.setDbName("my/db/12b");
        db.attach(SimpleEntity.class);
        db.start();

        SimpleEntity se = new SimpleEntity();
        se.id = "my id 2";
        se.myint = 1232;
        se.mylong = -1;
        se.myentry = "asd";
        se.otherEntry = null;

        se.register(db);
    }

}