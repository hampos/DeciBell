package org.kinkydesign.decibell.alpha.collect;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

public class Slave extends Component<Slave> {

    @PrimaryKey
    @NumericNull("-1")
    private int vInt = -1;

    @PrimaryKey
    private String vString;

    @Entry
    private String myEntry;

    public Slave() {
    }

    public Slave(int vInt, String vString, String myEntry) {
        this.vInt = vInt;
        this.vString = vString;
        this.myEntry = myEntry;
    }

    public String getMyEntry() {
        return myEntry;
    }

    public void setMyEntry(String myEntry) {
        this.myEntry = myEntry;
    }

    public int getvInt() {
        return vInt;
    }

    public void setvInt(int vInt) {
        this.vInt = vInt;
    }

    public String getvString() {
        return vString;
    }

    public void setvString(String vString) {
        this.vString = vString;
    }


}