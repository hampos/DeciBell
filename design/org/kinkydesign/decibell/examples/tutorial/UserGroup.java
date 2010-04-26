/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kinkydesign.decibell.examples.tutorial;

import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.Component;

/**
 *
 * @author hampos
 */
public class UserGroup extends Component{
    @PrimaryKey
    private int id=-1;

    @PrimaryKey
    private String name;

    public UserGroup() {
    }
   
    public UserGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    

    
}
