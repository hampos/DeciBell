package org.kinkydesign.decibell.alpha.fk1;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("C")
public class C extends Component<C> {

    @PrimaryKey
    @ForeignKey
    private D d;

    @PrimaryKey
    private String keyC;

    @Entry(notNull=true, defaultValue="XYZ-DEFAULT")
    private String xyz;

    public C() {
    }

    public C(D d, String keyC, String xyz) {
        this.d = d;
        this.keyC = keyC;
        this.xyz = xyz;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }

    public String getKeyC() {
        return keyC;
    }

    public void setKeyC(String keyC) {
        this.keyC = keyC;
    }

    public String getXyz() {
        return xyz;
    }

    public void setXyz(String xyz) {
        this.xyz = xyz;
    }


}