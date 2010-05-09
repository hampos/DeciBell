/**
 *  Class : SubEntity
 *  Date  : May 1, 2010
 *   .       .     ..
 *  _| _  _.*|_  _ ||
 * (_](/,(_.|[_)(/,||
 *
 * DeciBell : A Java Tool for creating and managing relational databases.
 *  DeciBell is a Object - Relation database mapper for java applications providing
 * an easy-to-use interface making it easy for the developer to build a relational
 * database and moreover perform database operations easily!
 *  This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Charalampos Chomenides & Pantelis Sopasakis
 *                         kinkyDesign ~ OpenSource Development

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * hampos Att yahoo d0t c0m
 * chvng att mail D0t ntua dd0T gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.kinkydesign.decibell.examples.subclassing;

import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.collections.OnModification;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SubEntity extends Entity {

    // TODO: Registration of non-direct subclasses of Component!
    @Entry
    private String info = null;

    @NumericNull(numericNullValue = "-1")
    @Entry
    private double xyz = -1;

    @ForeignKey()
    private RemoteEntity remote = null;
    
    @ForeignKey(onDelete = OnModification.CASCADE)
    private SubEntity subremote = null;

    public SubEntity() {
    }

    public SubEntity(String message, int number) {
        super(message, number);
    }
    

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public RemoteEntity getRemote() {
        return remote;
    }

    public void setRemote(RemoteEntity remote) {
        this.remote = remote;
    }

    public SubEntity getSubremote() {
        return subremote;
    }

    public void setSubremote(SubEntity subremote) {
        this.subremote = subremote;
    }

    public double getXyz() {
        return xyz;
    }

    public void setXyz(double xyz) {
        this.xyz = xyz;
    }


}
