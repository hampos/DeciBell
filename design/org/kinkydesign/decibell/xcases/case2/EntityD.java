package org.kinkydesign.decibell.xcases.case2;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("D")
public class EntityD extends Component<EntityD> {

    @PrimaryKey
    public String id;

    @Entry(defaultValue = "10.1")
    @NumericNull("-1")
    public double doubleValue;

    @Entry(defaultValue = "14")
    @NumericNull("-1")
    public long longValue;

    public EntityD() {
    }

    public EntityD(String id, double doubleValue, long longValue) {
        this.id = id;
        this.doubleValue = doubleValue;
        this.longValue = longValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }
}
