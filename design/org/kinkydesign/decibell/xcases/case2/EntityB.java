package org.kinkydesign.decibell.xcases.case2;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("B")
public class EntityB extends Component<EntityB>{

    @PrimaryKey
    public String id;

    @Entry
    public String myEntry;

    public EntityB() {
    }

    public EntityB(String id, String myEntry) {
        this.id = id;
        this.myEntry = myEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyEntry() {
        return myEntry;
    }

    public void setMyEntry(String myEntry) {
        this.myEntry = myEntry;
    }

    

}