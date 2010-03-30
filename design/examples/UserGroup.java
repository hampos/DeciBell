/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.Component;

/**
 *
 * @author hampos
 */
public class UserGroup extends Component{
    @PrimaryKey
    int id;

    @PrimaryKey
    String name;
}
