package org.kinkydesign.decibell.xcases.case0;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;

public class EntityWithCollection extends Component<EntityWithCollection> {

    @PrimaryKey
    private long uid;

    @ForeignKey
    Collection<Remote> remotes;

    public static void main(String... args) throws Exception {
        DeciBell db = new DeciBell();
        db.setDbName("my/db/huge");
        db.attach(EntityWithCollection.class);
        db.attach(Remote.class);
        db.start();


        Remote r[] = new Remote[1000];
        Collection<Remote> r_coll = new HashSet<Remote>(1000);
        for (int i = 0; i < 1000; i++) {
            r[i] = new Remote(UUID.randomUUID().toString(), System.nanoTime());
            r[i].attemptRegister(db);
            r_coll.add(r[i]);
        }

        EntityWithCollection e = new EntityWithCollection();
        e.remotes = r_coll;
        e.uid =  8800102;
        System.out.println("------------->~<-------------");
        long initTime = System.currentTimeMillis();
        e.register(db);
        long duration = System.currentTimeMillis() - initTime;
        System.out.println("Duration in ms : "+duration);





    }
}
