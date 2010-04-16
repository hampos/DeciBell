/**
 *  Class : InsertQuery
 *  Date  : Apr 8, 2010
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


package org.kinkydesign.decibell.db.query;

import java.util.LinkedHashMap;
import org.kinkydesign.decibell.db.Table;
import org.kinkydesign.decibell.db.TableColumn;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class InsertQuery  {

    private Table table;
   
    /**
     * A map from the table column to its corresponding value in the INSERT query,
     * e.g. (A, 2)
     */
    protected LinkedHashMap<TableColumn, String> ColumnValuesMap = new LinkedHashMap<TableColumn, String>();

    public InsertQuery(){
        
    }

    public InsertQuery(Table table){
        setTable(table);
    }

    
    public abstract  String getSQL();

    public void setTable(Table table) {
        this.table = table;
        ColumnValuesMap = new LinkedHashMap<TableColumn, String>();
        for (TableColumn tc : table.getTableColumns()){
            ColumnValuesMap.put(tc, null);
        }
    }

    public Table getTable() {
        return table;
    }
    

    public void setLong(TableColumn tableColumn, long longValue){
        if (!ColumnValuesMap.containsKey(tableColumn)){
            throw new IllegalArgumentException("TableColumn not in specified table!");
        }
        ColumnValuesMap.put(tableColumn, Long.toString(longValue));
    }

    public void setDouble(TableColumn tableColumn, double doubleValue){
        if (!ColumnValuesMap.containsKey(tableColumn)){
            throw new IllegalArgumentException("TableColumn not in specified table!");
        }
        ColumnValuesMap.put(tableColumn, Double.toString(doubleValue));
    }

    public void setInt(TableColumn tableColumn, int integerValue){
        if (!ColumnValuesMap.containsKey(tableColumn)){
            throw new IllegalArgumentException("TableColumn not in specified table!");
        }
        ColumnValuesMap.put(tableColumn, Integer.toString(integerValue));
    }

    public void setString(TableColumn tableColumn, String stringValue){
        if (!ColumnValuesMap.containsKey(tableColumn)){
            throw new IllegalArgumentException("TableColumn not in specified table!");
        }
        ColumnValuesMap.put(tableColumn, "'"+stringValue+"'");
    }



}