/**
 *  Class : Chung
 *  Date  : May 6, 2010
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


package org.kinkydesign.decibell.examples.enm;

import java.util.ArrayList;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.PrimaryKey;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Chung extends Component<Chung>{

    public enum STATUS {
        HIGH, LOW, MEDIUM;
    }

    @PrimaryKey private String id = null;

    @Entry private STATUS status = null;

    @Entry private ArrayList<String> xmlization = null;

    public Chung(){

    }

    public Chung(String id, STATUS status) {
        this();
        this.id = id;
        this.status = status;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getXmlization() {
        return xmlization;
    }

    public void setXmlization(ArrayList<String> xmlization) {
        this.xmlization = xmlization;
    }

    



}