
package org.kinkydesign.decibell;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Asdf {

    public static void main(String... args){
        DeciBell decibell = new DeciBell();
        decibell.attach(examples.User.class);
        decibell.attach(examples.UserGroup.class);
        decibell.start();
    }

}