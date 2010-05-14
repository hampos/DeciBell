package org.kinkydesign.decibell.xcases.case0;

import java.util.Random;
import java.util.UUID;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;

public class EntityWithObject extends Component<EntityWithObject> {

    @PrimaryKey
    @Entry(defaultValue="DEFAULT PK VALUE")
    public String id;

    @Entry
    public String myentry;

    @Entry(defaultValue="HAHAHAHA!")
    public String otherEntry;

    @Entry
    @NumericNull("-1")
    public int myint;

    @Entry(defaultValue="554")
    @NumericNull("-1")
    public long mylong;

    @Entry
    public Object myObject;

    public static void main(String... args) throws Exception{

        DeciBell db = new DeciBell();
        db.setDbName("my/db/12b75r");
        db.attach(EntityWithObject.class);
        db.start();

        EntityWithObject myEntity = new EntityWithObject();
        myEntity.id = UUID.randomUUID().toString();
        myEntity.myentry = "XXX";
        myEntity.myObject = null;

        
        myEntity.attemptRegister(db);

    }

}