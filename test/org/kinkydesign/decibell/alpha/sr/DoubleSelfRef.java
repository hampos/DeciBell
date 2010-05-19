package org.kinkydesign.decibell.alpha.sr;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DoubleSelfRef extends Component<DoubleSelfRef> {

    @PrimaryKey
    private String xKey;

    @PrimaryKey
    private String yKey;

    @Entry
    private String myEntry;

    @ForeignKey
    private DoubleSelfRef friend;

    public DoubleSelfRef() {
    }

    public DoubleSelfRef(String xKey, String yKey, String myEntry) {
        this.xKey = xKey;
        this.yKey = yKey;
        this.myEntry = myEntry;
    }

    public DoubleSelfRef getFriend() {
        return friend;
    }

    public void setFriend(DoubleSelfRef friend) {
        this.friend = friend;
    }

    public String getMyEntry() {
        return myEntry;
    }

    public void setMyEntry(String myEntry) {
        this.myEntry = myEntry;
    }

    public String getxKey() {
        return xKey;
    }

    public void setxKey(String xKey) {
        this.xKey = xKey;
    }

    public String getyKey() {
        return yKey;
    }

    public void setyKey(String yKey) {
        this.yKey = yKey;
    }

    

}
