package org.kinkydesign.decibell.xcases.case0;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;

@TableName("REMOTE")
public class Remote extends Component<Remote> {

    @PrimaryKey
    private String id;
    @Entry
    private long myEntry;

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
}
