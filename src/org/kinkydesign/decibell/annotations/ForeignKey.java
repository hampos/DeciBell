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
import org.kinkydesign.decibell.collections.OnModification;
import org.kinkydesign.decibell.core.Component;

/**
 *
 * A <code>foreign key</code> in an entity (component) is an attribute that points to
 * the primary key of another entity.
 *
 * <p>
 * An attribute or a set of attributes in some entity
 * , references an attribute or a set of attributes in another entity by a <code>foreign
 * key constraint</code>. The purpose of the foreign key is to ensure referential integrity
 * of the data i.e. only values that are supposed to appear in an attribute of some other
 * entity are permitted. Foreign keys are defined in the ANSI SQL Standard. Let us now give
 * an example of how you can use <code>@ForeignKey</code> in your source code. Suppose you have
 * two classes, namely <code>User</code> and <code>UserGroup</code> as follows:
 * </p>
 * <p>
 * <code>
 * class UserGroup {<br/><br/>
 * 
 *      public String groupName;<br/>
 *      public String authorizationLevel;<br/><br/>
 * 
 * }<br/><br/>
 * </code>
 * <code>
 * class User {<br/><br/>
 * 
 *      public String userName;<br/>
 *      public String password;<br/>
 *      public UserGroup group;<br/><br/>
 * 
 * }
 * </code>
 * </p>
 * 
 * <p>
 * The you can use the annotation <code>@{@link ForeignKey }</code> to declare that 
 * the field <code>public UserGroup group;</code> points to the entity <code>UserGroup</code>.
 * We note again here that every entity must be endowed with a <code>@{@link PrimaryKey }</code>.
 * So modifying slightly the above code, yields:
 * </p>
 * 
 * <p>
 * <code>
 * class UserGroup extends {@link Component } {<br/><br/></code>
 * 
 *      <code>@{@link PrimaryKey } public String groupName;<br/></code>
 *      <code>@{@link Entry } public String authorizationLevel;<br/><br/>
 * 
 * }<br/><br/>
 * </code>
 * <code>class User extends {@link Component } {<br/><br/></code>
 * 
 *      <code>@{@link PrimaryKey } public String userName;<br/></code>
 *      <code>@{@link Entry } public String password;<br/></code>
 *      <code>@{@link ForeignKey } public UserGroup group;<br/><br/></code>
 * 
 * }
 * </code>
 * </p>
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see <a href="http://en.wikipedia.org/wiki/Foreign_key">Wikipedia Article on Foreign Keys</a>
 * @see <a href="http://en.wikipedia.org/wiki/Entity-relationship_model">Wikipedia article on the ER data model</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
public @interface ForeignKey {

    /**
     * 
     * Define the behaviour to be automatically taken on delete.
     * @return
     *      Action to be automatically taken on delete
     */
    OnModification onDelete() default OnModification.NO_ACTION;

    /**
     * 
     * Define the behaviour to be automatically taken on update.
     * @return
     *       Action to be automatically taken on update
     */
    OnModification onUpdate() default OnModification.NO_ACTION;


}
