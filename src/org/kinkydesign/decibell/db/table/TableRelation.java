/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kinkydesign.decibell.db.table;

/**
 *
 * @author hampos
 */
public class TableRelation extends Table{
    private Table first = null;
    private Table second = null;

    public TableRelation(Table first, Table second){
        this.first = first;
        this.second = second;
    }


}
