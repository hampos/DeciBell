package org.kinkydesign.decibell.alpha.fk1;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("B")
public class B extends Component<B> {

    @PrimaryKey
    private String keyB;

    @Entry
    private String entryB;

    public B() {
    }

    public B(String keyB, String entryB) {
        this.keyB = keyB;
        this.entryB = entryB;
    }

    public String getEntryB() {
        return entryB;
    }

    public void setEntryB(String entryB) {
        this.entryB = entryB;
    }

    public String getKeyB() {
        return keyB;
    }

    public void setKeyB(String keyB) {
        this.keyB = keyB;
    }

    


}