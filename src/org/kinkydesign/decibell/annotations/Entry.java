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
import org.kinkydesign.decibell.Component;

/**
 *
 * <p align="justify" style="width:60%">
 * An <em>attribute</em> in ER-terms, that is a characteristic of an entity we need to
 * write to/read from the relational database.
 * </p>
 *
 * <p align="justify" style="width:60%">
 * Note: Database creation takes into account all fields annotated with the <code>@Entry</code>
 * annotation to create a proper structure that allows this information to be registered
 * in and/or be read from the database. No knowledge of the database structure is needed,
 * once you understand the relations between the classes you have defined, that is the
 * normal java hierarchical structure.
 * </p>
 * <p align="justify" style="width:60%">
 * Here we provide a brief example of use of this annotation, its meaning and
 * consequences. So suppose one wants to maintain a database that stores information
 * about customers. Every customer has a name, id, phone number and a debt. The customer is also characterized
 * by his/her age but consider that this information is not of any specific interest (at
 * least for the time). This naturally leads
 * to the introduction of the following java class in ones code:
 * </p>
 *
 * <p align="justify" style="width:60%">
 * <code>class Customer {<br/><br/>
 *
 *   public String name;<br/>
 *   public int id;<br/>
 *   public String phoneNumber;<br/>
 *   public int debt;<br/>
 *   public int age;<br/><br/>
 *
 * }
 * </code>
 * </p>
 *
 * <p align="justify" style="width:60%">
 * If we need to store information contained in Customer type objects and retreive
 * such information from the database, we have to modify the class <code>Customer</code>
 * a little bit. First of all, the class Customer shall become subclass of {@link Component }
 * and the annotation <code>@Entry</code> will be used to declare which information we
 * need to be used by DeciBell&copy;. Note that, in any case one of the entries has to
 * be characterized as {@link PrimaryKey primary key}. Assume that here <code>id</code>
 * will act as the primary key. Here is the modified code for the class <code>Customer</code> :
 * </p>
 * <p align="justify" style="width:60%">
 * <code>class Customer extends {@link Component } {</code><br/><br/>
 *
 *   <code>@{@link Entry }({@link Entry#unique() unique } = true, {@link Entry#notNull() notNull} = true) public String name;</code><br/>
 *   <code>@{@link PrimaryKey } public int id;</code><br/>
 *   <code>@{@link Entry } public String phoneNumber;</code><br/>
 *   <code>@{@link Entry } public int debt;</code><br/>
 *   <code>public int age;<br/><br/>
 *
 * }
 * </code>
 * </p>
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see Component
 * @see PrimaryKey
 * @see ForeignKey
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
public @interface Entry {

    /**
     *
     * <p align="justify" style="width:60%">
     * The key is unique among all entities. Every field annotated with the <code>@Entry</code>
     * annotation with the extra characterization <code>unique=true</code> should have
     * unique values over all instances of that class, at least when database registration
     * operations are performed.
     * </p>
     *
     * @return 
     *      <code>true</code> if is unique
     */
    boolean unique() default false;
    
    /**
     * This key cannot be null
     * @return true if is not null
     */
    boolean notNull() default false;

    /**
     *
     * Default value for the annotated field
     *
     * @return
     *      Value to be assigned to the key if no other value is provided.
     */
    String defaultValue() default "";

    
    
}

