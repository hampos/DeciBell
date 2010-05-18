package org.kinkydesign.decibell.xcases.case0;

import java.util.UUID;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;

@TableName("ENTITY")
public class EntityWithForeignKey extends Component<EntityWithForeignKey>{

    @PrimaryKey
    public String myid;

    @ForeignKey
    public Remote remote;


    public static void main(String... args) throws Exception{

        DeciBell db = new DeciBell();
        db.setDbName("my/db/fr3a66h1");
        db.attach(EntityWithForeignKey.class);
        db.attach(Remote.class);
        db.start();

        Remote remote = new Remote();
        remote.setId(UUID.randomUUID().toString());
        remote.setMyEntry(15423);
        remote.attemptRegister(db);

        EntityWithForeignKey ent = new EntityWithForeignKey();
        ent.myid = UUID.randomUUID().toString();
        ent.remote = remote;
        ent.attemptRegister(db);


        System.out.println(new EntityWithForeignKey().search(db));

    }

}