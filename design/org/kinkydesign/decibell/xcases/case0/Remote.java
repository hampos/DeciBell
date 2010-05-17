package org.kinkydesign.decibell.xcases.case0;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;
import org.kinkydesign.decibell.db.sieve.JSieve;

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
        db.setDbName("my/db/13bcd921");
        db.attach(Remote.class);
        db.start();

        Remote r1 = new Remote(null, 13);
        Remote r2 = new Remote("hello", 14);
        Remote r3 = new Remote("something", 15);
        r1.attemptRegister(db);
        r2.attemptRegister(db);
        r3.attemptRegister(db);

        JSieve<Remote> sieve = new JSieve<Remote>() {

            public boolean sieve(Remote component) {
                if (component.getMyEntry() >=14 && component.getMyEntry() <= 15){
                    return true;
                }
                return false;
            }
        };

        System.out.println(new Remote("%", -1).search(db,sieve));
    }
}
