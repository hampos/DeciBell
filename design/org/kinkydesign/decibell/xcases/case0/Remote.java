package org.kinkydesign.decibell.xcases.case0;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;

@TableName("REMOTE")
public class Remote extends Component<Remote> {

    @PrimaryKey
    private String id;
    @Entry
    @NumericNull("-1")
    private long myEntry = -1;

    public Remote() {
    }

    public Remote(String id, long myEntry) {
        this.id = id;
        this.myEntry = myEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMyEntry() {
        return myEntry;
    }

    public void setMyEntry(long myEntry) {
        this.myEntry = myEntry;
    }

    public static void main(String... args) throws Exception{
        DeciBell db = new DeciBell();
        db.setDbName("my/db/13bcd920");
        db.attach(Remote.class);
        db.start();

        Remote r1 = new Remote("abc", 13);
        Remote r2 = new Remote("def", 14);
        Remote r3 = new Remote("hij", 15);
        r1.attemptRegister(db);
        r2.attemptRegister(db);
        r3.attemptRegister(db);

        System.out.println(new Remote("%a%", -1).search(db));
    }
}
