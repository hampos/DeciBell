package org.kinkydesign.decibell.alpha.fk1;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("D")
public class D extends Component<D> {

    @PrimaryKey
    private String keyD1;

    @PrimaryKey
    private String keyD2;

    @Entry
    @Constraint(low="-100", high="100")
    @NumericNull("-1")
    private double number = -1;

    public D() {
    }

    public D(String keyD1, String keyD2, double number) {
        this.keyD1 = keyD1;
        this.keyD2 = keyD2;
        this.number = number;
    }

    public String getKeyD1() {
        return keyD1;
    }

    public void setKeyD1(String keyD1) {
        this.keyD1 = keyD1;
    }

    public String getKeyD2() {
        return keyD2;
    }

    public void setKeyD2(String keyD2) {
        this.keyD2 = keyD2;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }


}