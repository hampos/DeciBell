package org.kinkydesign.decibell.alpha.collect;

import java.util.List;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;

public class Master extends Component<Master> {

    @PrimaryKey
    private String id;

    @ForeignKey
    private List<Slave> slaves;

    public Master() {
    }

    public Master(String id, List<Slave> slaves) {
        this.id = id;
        this.slaves = slaves;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Slave> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<Slave> slaves) {
        this.slaves = slaves;
    }

}