package org.kinkydesign.decibell.alpha.fk1;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.*;

@TableName("A")
public class A extends Component<A>{

    @PrimaryKey
    private String aKey;

    @ForeignKey
    private B bLink;

    @ForeignKey
    private C cLink;

    public A() {
    }

    public A(String aKey, B bLink, C cLink) {
        this.aKey = aKey;
        this.bLink = bLink;
        this.cLink = cLink;
    }

    public String getaKey() {
        return aKey;
    }

    public void setaKey(String aKey) {
        this.aKey = aKey;
    }

    public B getbLink() {
        return bLink;
    }

    public void setbLink(B bLink) {
        this.bLink = bLink;
    }

    public C getcLink() {
        return cLink;
    }

    public void setcLink(C cLink) {
        this.cLink = cLink;
    }

    

}