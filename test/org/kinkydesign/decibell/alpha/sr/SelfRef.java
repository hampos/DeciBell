/**
 *  Class : SelfRef
 *  Date  : May 19, 2010
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


package org.kinkydesign.decibell.alpha.sr;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SelfRef extends Component<SelfRef> {

    @PrimaryKey
    @NumericNull("-1")
    @Entry(defaultValue="465")
    private int myid = -1;

    @Entry(defaultValue="A-DEFAULT",notNull=true)
    private String a;

    @Entry(defaultValue="B-DEFAULT")
    private String b;

    @Entry(defaultValue="C_DEFAULT")
    private String c;

    @Entry
    @NumericNull("-1")
    private long longValue = -1;

    @ForeignKey
    private SelfRef friend;
    

    public SelfRef() {
    }

    public SelfRef(int myid) {
        this.myid = myid;
    }

    public SelfRef(int myid, String a, String b, String c) {
        this.myid = myid;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public SelfRef getFriend() {
        return friend;
    }

    public void setFriend(SelfRef friend) {
        this.friend = friend;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }


}