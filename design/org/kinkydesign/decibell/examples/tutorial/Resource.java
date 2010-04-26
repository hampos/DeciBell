/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kinkydesign.decibell.examples.tutorial;

import java.io.Serializable;

/**
 *
 * @author hampos
 */
public class Resource {
        private int id=-1;
        private String whatever;

        private Pool pool = new Pool();

        public Resource(int id, String whatever){
            this.id = id;
            this.whatever = whatever;
        }
}
