/**
 *  Class : tester
 *  Date  : Apr 24, 2010
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


package org.kinkydesign.decibell.examples.yaqp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.DeciBell;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Tester {

    public static void main(String... args) throws DuplicateKeyException, URISyntaxException, IOException{
        DeciBell db = new DeciBell();
        db.setDriverHome("/home/chung/JLib/10.6.0.0alpha_2010-02-15T19-30-14_SVN910262");
        db.setDbName("database/s");

        db.attach(ErrorReport.class);
        db.attach(ErrorCode.class);
        db.attach(Task.class);

        db.start();
        

        ErrorCode ec = new ErrorCode();
        ec.code = 12342;
        ec.httpStatus = 500;
        ec.message = "unknown error";
        //ec.register(db);

        ErrorReport er = new ErrorReport();
        er.uid = 24124;
        er.errorCode=ec;
        er.trace = er;
        er.actor = "none";
        er.details="details";
        //er.register(db);

        ErrorReport er2 = new ErrorReport();
        er2.uid = 1101;
        er2.errorCode=ec;
        er2.trace = er2;
        er2.actor = "nun";
        er2.details="detrails";
        //er2.register(db);

        ErrorReport er3 = new ErrorReport();
        er3.uid = 565758;
        er3.errorCode=ec;
        er3.trace = er2;
        er3.actor = "someone";
        er3.details="message of details";
        //er3.register(db);
        
               
        ArrayList<? extends Component> list = new ErrorReport().search(db);
        System.out.println("Error message"+((ErrorReport)list.get(0)).trace.trace.trace.trace.trace.trace.errorCode.message);

        Task t = new Task();
        t.durationMS = 918;
        t.er = er3;
        t.resultURI = new URI("http://hampos.org");
        t.taskStatus = 812;
        t.timeFinish = 6612L;
        t.timeStart = 900101;
        t.uid=22210;
        //t.register(db);

        ArrayList<? extends Component> list2 = new Task().search(db);
        list2.get(2).print(System.out);

                       
    }

    


}