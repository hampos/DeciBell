package org.kinkydesign.decibell.xcases.case2;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("C")
public class EntityC extends Component<EntityC>{

    @PrimaryKey
    @ForeignKey
    public EntityD entityD;

    @PrimaryKey
    public String myValue;

    @Entry
    public String value;

    public EntityC() {
    }

    public EntityC(EntityD entityD, String myValue, String value) {
        this.entityD = entityD;
        this.myValue = myValue;
        this.value = value;
    }

    public EntityD getEntityD() {
        return entityD;
    }

    public void setEntityD(EntityD entityD) {
        this.entityD = entityD;
    }

    public String getMyValue() {
        return myValue;
    }

    public void setMyValue(String myValue) {
        this.myValue = myValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    

}