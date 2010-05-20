package org.kinkydesign.decibell.alpha.sr;

import java.util.ArrayList;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;

@TableName("SRL")
public class SRList extends Component<SRList>{

    @PrimaryKey
    private String id;

    @ForeignKey
    private ArrayList<SRList> myList;

    public SRList() {
    }

    public SRList(String id, ArrayList<SRList> myList) {
        this.id = id;
        this.myList = myList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<SRList> getMyList() {
        return myList;
    }

    public void setMyList(ArrayList<SRList> myList) {
        this.myList = myList;
    }



}