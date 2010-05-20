package org.kinkydesign.decibell.xcases.case0;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;

public class EntityWithCollection extends Component<EntityWithCollection> {

    @PrimaryKey
    @Entry(autoGenerated=true)
    @NumericNull("-1")
    private long uid = -1;

    @Entry
    private String comment = "no comment";

    @ForeignKey
    Collection<Remote> remotes;

    public EntityWithCollection() {
    }

    public EntityWithCollection(long uid, Collection<Remote> remotes) {
        this.uid = uid;
        this.remotes = remotes;
    }

    

    public static void main(String... args) throws Exception {
        DeciBell db = new DeciBell();
        db.setDbName("my/db/541ghr5e6");
        db.attach(EntityWithCollection.class);
        db.attach(Remote.class);
        db.start();

        Remote r[] = new Remote[50];
        Collection<Remote> r_coll = new HashSet<Remote>(50);
        for (int i = 0; i < 50; i++) {
            r[i] = new Remote(UUID.randomUUID().toString(), System.nanoTime());
            r[i].attemptRegister(db);
            r_coll.add(r[i]);
        }

        EntityWithCollection e = new EntityWithCollection();
        e.remotes = r_coll;
        e.uid =  90102;
        System.out.println("------------->~<-------------");
        long initTime = System.currentTimeMillis();
        e.register(db);
        long duration = System.currentTimeMillis() - initTime;
        System.out.println("Duration = "+duration+"ms");

        
        //System.out.println(new EntityWithCollection().search(db));
        new EntityWithCollection().search(db).get(0).print(System.err);
        
    }

}
