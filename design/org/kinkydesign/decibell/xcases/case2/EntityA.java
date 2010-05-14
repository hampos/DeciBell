package org.kinkydesign.decibell.xcases.case2;

import java.util.UUID;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.*;
import org.kinkydesign.decibell.exceptions.ImproperDatabaseException;

@TableName("A")
public class EntityA extends Component<EntityA>{

    @PrimaryKey
    private String id;

    @ForeignKey
    private EntityC myc;

    public EntityA() {
    }

    public EntityA(String id, EntityC myc) {
        this.id = id;
        this.myc = myc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EntityC getMyc() {
        return myc;
    }

    public void setMyc(EntityC myc) {
        this.myc = myc;
    }



    public static void main(String... args) throws Exception{
        EntityD d = new EntityD(UUID.randomUUID().toString(), 14.2, 9231001);
        EntityC c = new EntityC(d, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        EntityB b = new EntityB(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        EntityA a = new EntityA(UUID.randomUUID().toString(), c);

        DeciBell db = new DeciBell();
        db.setDbName("my/db/fjsdhg324");
        db.attachFromPackages("org.kinkydesign.decibell.xcases.case2");

        db.start();

        d.attemptRegister(db);
        c.attemptRegister(db);
        b.attemptRegister(db);
        a.attemptRegister(db);
        

    }
    
    

}