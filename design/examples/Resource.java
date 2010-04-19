/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import java.io.Serializable;

/**
 *
 * @author hampos
 */
public class Resource {
        private int id;
        private String whatever;

        private Pool pool = new Pool();

        public Resource(int id, String whatever){
            this.id = id;
            this.whatever = whatever;
        }
}
