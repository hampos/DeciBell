/**
 *
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.kinkydesign.decibell.db.interfaces;

import org.kinkydesign.decibell.db.table.TableColumn;
import org.kinkydesign.decibell.collections.Qualifier;

/**
 *
 * An sql-specific proposition that consists of three parts: the column, the qualifier,
 * and the value. The column is an instance of {@link TableColumn}, the qualifier is a
 * binary relation from the enumeration {@link Qualifier} and the value is some java object
 * (integer, double, long, String etc), or <code>null</code> or <code>unknown</code>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JProposition {

    /**
     * The qualifier of the proposition, i.e. an element of the enumeration {@link Qualifier}.
     * @return
     *      qualifier of the proposition
     */
    Qualifier getQualifier();

    /**
     *
     * The table column participating in the proposition.
     * @return
     *      Table column of the sql proposition .
     */
    TableColumn getTableColumn();

    /**
     * Set an integer value for the proposition.
     * @param longValue
     * Value for the proposition.
     * @throws IllegalArgumentException
     * In case the type of the table column is such that cannot accept the
     * integer value. Use this method only for integer-type and long-type
     * (bigInt) table columns.
     */
    void setInt(long longValue) throws IllegalArgumentException;

    /**
     * Sets a value to <code>NULL</code> (SQL null). Here is an example of <code>setNull()</code>
     * and the resulting Proposition:
     * <pre><code>
     * // Initialize the Proposition Object using the
     * // implementation Proposition
     * JProposition p = new Proposition();
     *
     * // Introduce the table column
     * TableColumn tc = new TableColumn("PARAM");
     * tc.setColumnType(SQLType.VARCHAR);
     *
     * // Configure the proposition
     * p.setTableColumn(tc);
     * p.setNull();
     * p.setQualifier(Qualifier.IS);
     *
     * System.out.println(p);</code></pre>
     * This example prints out: <code>PARAM IS NULL</code>. Note that when using this
     * method, the Qualifier you choose must be one of {@link Qualifier#IS is} or
     * {@link Qualifier#IS_NOT is not} otherwise an exception is thrown (see below).
     *
     * @throws IllegalArgumentException
     *      In case the table column is not nullable or the qualifier is not appropriate
     *      for <code>null</code> values.
     *
     * @see JProposition#setUnknown() set unknown value
     */
    void setNull() throws IllegalArgumentException;

    /**
     * Set a numeric value for the proposition
     * @param numericValue
     * @throws IllegalArgumentException
     * In case the parameter cannot be set
     */
    void setDouble(double numericValue) throws IllegalArgumentException;

    /**
     * Set the qualifier of the proposition, e.g. {@link Qualifier#EQUAL }.
     * @param qualifier
     *      The qualifier for the proposition.
     */
    void setQualifier(Qualifier qualifier);

    /**
     * Set a string value. String values in propositions are quoted in single quotes.
     * For example the invokation of setString("abc") is some query will result in
     * a proposition like <code>X = 'abc'</code>.
     * @param stringValue
     *      String value.
     */
    void setString(String stringValue);

    /**
     * Set the value of the proposition to <code>unknown</code>. The symbol of
     * <code>unknown</code> is the question mark (?). So, if this method is invoked
     * on some proposition, the result will look like <code>X = ?</code>.
     */
    void setUnknown();

    /**
     * Define the subject of the proposition, that is a table column.
     * @param tableColumn
     *      The subject of the proposition.
     */
    void setTableColumn(TableColumn tableColumn);

    /**
     * A string representation of the proposition. For example <code>PARAM_1 = 10</code>
     * @return
     *      The proposition as a String.
     */
    @Override
    String toString();

}
