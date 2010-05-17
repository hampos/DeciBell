package org.kinkydesign.decibell.xcases.case0;

import java.util.UUID;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;


@TableName("SR")
public class SelfRef extends Component<SelfRef> {

    @PrimaryKey
    private String id;
    
    @ForeignKey
    private SelfRef other;

    public SelfRef() {
    }

    public SelfRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SelfRef getOther() {
        return other;
    }

    public void setOther(SelfRef other) {
        this.other = other;
    }

    public static void main(String... args) throws Exception{
        DeciBell db = new DeciBell();
        db.setDbName("my/db/ababvvg332b1");
        db.attach(SelfRef.class);
        db.start();

        SelfRef a = new SelfRef();
        a.setId(UUID.randomUUID().toString());
        a.setOther(new SelfRef("6422fec0-9394-474a-b238-4e93bac11c97"));
        a.register(db);
        
        SelfRef proto = new SelfRef();
        proto.setId("%c%");
        SelfRef p = new SelfRef();
        System.out.println(p.search(db));
    }

    

}