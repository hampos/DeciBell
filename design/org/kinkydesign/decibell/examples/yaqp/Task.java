/**
 *  Class : Task
 *  Date  : Apr 25, 2010
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

import java.net.URI;
import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@TableName("TSK")
public class Task extends Component<Task> {

    @PrimaryKey
    @NumericNull(numericNullValue = "-1")
    private int uid = -1;
    @Entry
    @NumericNull(numericNullValue = "-1")
    private int taskStatus = -1;
    @Entry
    private URI resultURI;
    @ForeignKey
    private ErrorReport er;
    @Entry
    @NumericNull(numericNullValue = "-1")
    private long timeStart = -1L;
    @Entry
    @NumericNull(numericNullValue = "-1")
    private long timeFinish = -1L;
    @Entry
    @NumericNull(numericNullValue = "-1")
    private long durationMS = -1L;

    public Task() {
    }

    public long getDurationMS() {
        return durationMS;
    }

    public void setDurationMS(long durationMS) {
        this.durationMS = durationMS;
    }

    public ErrorReport getEr() {
        return er;
    }

    public void setEr(ErrorReport er) {
        this.er = er;
    }

    public URI getResultURI() {
        return resultURI;
    }

    public void setResultURI(URI resultURI) {
        this.resultURI = resultURI;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public long getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(long timeFinish) {
        this.timeFinish = timeFinish;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
