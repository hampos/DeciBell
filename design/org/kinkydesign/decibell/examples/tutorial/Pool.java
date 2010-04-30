package org.kinkydesign.decibell.examples.tutorial;

import java.util.ArrayList;

public class Pool extends Resource {
    ArrayList<Integer> list = new ArrayList<Integer>();

    public Pool(int id, String whatever){
        super(id, whatever);
        list.add(1);
        list.add(3);
        list.add(13);
    }
}
