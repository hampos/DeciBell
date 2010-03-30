/**
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
package org.kinkydesign.decibell.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Establish a constraint for a key. The constraint can either be numeric or
 * nominal. For example <code>@Constraint(domain={"A", "B", "C"})</code> is a nominal constraint
 * demanding that the underlying variable takes values from the set <code>{A, B, C}</code>. The
 * constraint <code>@Constraint(low=10, high=20)</code> means that the variable is constrained in
 * the closed interval [10,20].
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
public @interface Constraint {

    /**
     * Lower-bound imposed on an attribute as a constraint. Applies only to numeric-type
     * attributes and should not be combined with {@link Constraint#domain() domain()}.
     * @return
     *      String value which must be castable as a number. I.e. "1" is an acceptable
     *      value while "a" is not.
     * @see Constraint#high()
     */
    String low() default "";

    /**
     * Higher-bound imposed on an attribute as a constraint. Applies only to numeric-type
     * attributes and should not be combined with {@link Constraint#domain() domain()}.
     * @return
     *      String value which must be castable as a number. I.e. "1" is an acceptable
     *      value while "a" is not.
     * @see Constraint#low()
     */
    String high() default "";

    /**
     * Specify the domain of nominal attributes, that is attributes that accept
     * values from a finite set. Examples of such values is status-like values. For
     * example if the attribute under consideration is a HTTP status then it could
     * be constrained in the set <code>{200, 400, 404, 500}</code>; or if it is the
     * "opinion of a customer for a product" the <code>domain</code> would be
     * something like <code>{"GOOD", "BAD", "NO ANSWER"}</code>.
     *
     * @return
     *      The domain of the attribute as a string array.
     */
    String[] domain() default {""};


}
