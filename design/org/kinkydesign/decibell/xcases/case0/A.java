package org.kinkydesign.decibell.xcases.case0;

import java.util.*;
import org.kinkydesign.decibell.*;
import org.kinkydesign.decibell.annotations.*;

@TableName("A")
public class A extends Component<A> {

    @PrimaryKey
    private String a;

    @ForeignKey
    private A same;

    @ForeignKey
    private Collection<B> list = new ArrayList<B>();

    public A() {
    }

    public A(String a, A same) {
        this.a = a;
        this.same = same;
    }


    public static void main(String... args) throws Exception{
        DeciBell db = new DeciBell();
        db.setDbName("my/simplefk/a1a3");
        db.attach(A.class);
        db.attach(B.class);
        db.start();


        B b1 = new B("b1");
        B b2 = new B("b2"); 
        b1.attemptRegister(db);
        b2.attemptRegister(db);
        ArrayList<B> bList = new ArrayList<B>();
        bList.add(b1);
        bList.add(b2);

        A a1 = new A("a2", new A());
        a1.same = a1;
        a1.list = bList;
        a1.attemptRegister(db);


        System.out.println(new A("%", null).search(db));
    }
    

}

