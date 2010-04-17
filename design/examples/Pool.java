package examples;

import java.io.Serializable;

public class Pool implements Serializable{
    Object obj;

    public Pool(Object obj){
        this.obj = obj;
    }
}
