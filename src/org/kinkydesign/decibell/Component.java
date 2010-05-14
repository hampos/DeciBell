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
package org.kinkydesign.decibell;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.annotations.NumericNull;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.db.engine.DeletionEngine;
import org.kinkydesign.decibell.db.engine.RegistrationEngine;
import org.kinkydesign.decibell.db.engine.SearchEngine;
import org.kinkydesign.decibell.db.engine.UpdateEngine;
//import org.kinkydesign.decibell.db.engine.XSearchEngine;
import org.kinkydesign.decibell.exceptions.DuplicateKeyException;
import org.kinkydesign.decibell.exceptions.ImproperRegistration;
import org.kinkydesign.decibell.exceptions.NoUniqueFieldException;

/**
 * <p  align="justify" style="width:60%">
 * Any class that needs to be persistent in the database using DeciBell&copy; has to
 * subclass (directly or indirectly) the class Component. Component contains methods
 * that allow directly to read from and write to the database, as well as, delete and update
 * one or more database entries. The user does not need to interact directly or by any means
 * with the database it self or know anything about the database structure, SQL queries and
 * java-to-db-to-java data transactions whatsoever.
 * </p>
 * <p  align="justify" style="width:60%">
 * For increased security and performance, <code>SELECT</code>, <code>INSERT</code>,
 * <code>UPDATE</code> and <code>DELETE</code> queries are prepared on startup. The prepared
 * statements are supplied with parameters using the Java reflection API.
 * </p>
 * <p  align="justify" style="width:60%">
 * Although the read/write/delete/update procedure seems to be quite opaque (indeed
 * , it is intended to work as a black box tool), it is possible to access the database
 * directly or even connect to it using <em>ij</em> (Derby Interace for connecting to
 * a JDBC database and performing SQL operations).
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Component#register(org.kinkydesign.decibell.DeciBell) insert
 * @see Component#search(org.kinkydesign.decibell.DeciBell)  select
 * @see Component#delete(org.kinkydesign.decibell.DeciBell) delete
 * @see Component#update(org.kinkydesign.decibell.DeciBell) update
 */
public abstract class Component<T extends Component> implements Cloneable {

    /**
     * Empty constructor for the class Component.
     */
    public Component() {
    }

    /**
     *
     * <p  align="justify" style="width:60%">
     * Deletes the current component from the database. At least one valid unique field must
     * be specified in order for the delete operation to take place.
     * This operation is based on prepared statements.
     * </p>
     * @param db
     *      The decibell object which identifies a database connection
     * @throws NoUniqueFieldException
     *      In case there is no identifier for the object to be deleted. No primary
     *      key or unique field was specified.
     * @see DeletionEngine
     */
    public void delete(DeciBell db) throws NoUniqueFieldException {
        DeletionEngine engine = new DeletionEngine(db);
        engine.delete(this);
    }

    /**
     *
     * <p  align="justify" style="width:60%">
     * Registers the component in the specified database. Note that if some field
     * in the component posseses a declaration of a <code>DEFAULT</code> value and
     * you provide a <code>null</code> value for registration, the default value
     * is used instead. Also if a numeric field (e.g. int, double, float etc) has a
     * declared null value using the annotation @{@link NumericNull }, this value
     * stands for <code>null</code>, that is if you attempt to register this value,
     * and you have declared a <code>DEFAULT</code> value, the default value will
     * be registered.
     * </p>
     * <p  align="justify" style="width:60%">
     * We prompt the users of DeciBell to always excplicitly denote the value of @{@link NumericNull }
     * for every numeric field and also provide a default value
     * for both numeric and String fields as in the current version of DeciBell there
     * is no support for registering <code>null</code> values in the database; instead
     * some default values are stored.
     * </p>
     * @param db
     *      The decibell object which identifies a database connection.
     * @throws DuplicateKeyException
     *      In case the component is already registered in the database. If you
     *      need to modify a component identified by some primary key attribute which
     *      is already in the database, consider using the method
     *      {@link Component#update(org.kinkydesign.decibell.DeciBell) update} instead.
     * @throws ImproperRegistration
     *      In case the component cannot be registered in the database. This is the
     *      case when the candidate object posseses a null Collection-type field.
     * @see Component#attemptRegister(org.kinkydesign.decibell.DeciBell) attemptRegister
     * @see RegistrationEngine
     */
    public void register(DeciBell db) throws DuplicateKeyException, ImproperRegistration {
        RegistrationEngine engine = new RegistrationEngine(db);
        engine.register(this);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Attempts to register the component in the database.
     * An invokation of {@link Component#register(org.kinkydesign.decibell.DeciBell) register} masking
     * the {@link DuplicateKeyException Duplicate Key Exception}. Returns an integer flag to tell whether
     * the component is already registered (or a component with the same primary key is registered) or
     * if the component was registered in the database for the first time.
     * </p>
     * @param db
     *      The decibell object which identifies a database connection.
     * @return
     *      An integer identifier of whether the component was registered or not. Returns <code>0</code>
     *      if the given component was registered and <code>1</code> if it was not registered due to a
     *      duplicate key exception thrown (which was masked).
     * @throws ImproperRegistration
     *      In case the component cannot be registered in the database. This is the
     *      case when the candidate object posseses a null Collection-type field.
     * @see Component#register(org.kinkydesign.decibell.DeciBell) register
     * @see RegistrationEngine
     */
    public int attemptRegister(DeciBell db) throws ImproperRegistration {
        RegistrationEngine engine = new RegistrationEngine(db);
        try {
            engine.register(this);
            return 0;
        } catch (DuplicateKeyException ex) {
            return 1;
        }
    }

    /**
     *
     * <p  align="justify" style="width:60%">
     * Get all the components from the database which <em>resemble</em> the given component
     * on which the <code>search</code> method is applied.
     * The current component is used as a <em>prototype</em> for the performed search. Any
     * fields set to <code>null</code> are ommited throughout the search. Of course you
     * cannot assign a <code>null</code> value to a primitive-type field (like <code>int, long
     * ,float, double</code>) but you can use its numericNull value instead specified by
     * the annotation {@link NumericNull }. This search is based on prepared statements
     * for increased performance and security. These statements are prepared upon
     * the startup of decibell ({@link DeciBell#start() start DeciBell}).
     * </p>
     * @param db
     *      The decibell object which identifies a database connection
     * @return
     *      List of objects found in the database
     * @see SearchEngine
     * @see NumericNull
     */
    public ArrayList<T> search(DeciBell db) {
        if (Component.class.equals(this.getClass().getSuperclass())) { // Direct subclass of Component
            SearchEngine<T> engine = new SearchEngine<T>(db);
            return engine.search(this);
        } else {// Indirect subclass of component
            //XSearchEngine<T> engine = new XSearchEngine<T>(db);
            return null;
        }
    }

    public ArrayList<T> find(DeciBell db) {
            SearchEngine<T> engine = new SearchEngine<T>(db);
            return new ArrayList<T>(engine.find(this));
    }

    /**
     * <p  align="justify" style="width:60%">
     * Update a single database entry identified by some primary key or unique field
     * value.
     * </p>
     * @throws NoUniqueFieldException
     *      In case the update operation does not uniquely identify a ccomponent
     *      to be updated. No primary key or unique field was specified for the
     *      component on which the method is applied
     * @see UpdateEngine
     */
    public void update(DeciBell db) throws NoUniqueFieldException, DuplicateKeyException {
        UpdateEngine engine = new UpdateEngine(db);
        engine.update(this);
    }

    /**
     * <p  align="justify" style="width:60%">
     * Prints the component to some PrintStream. You can use this method to print
     * the component to some generic OutputStream:
     * <blockquote><pre>
     * OutputStream os = ...;
     * Component component = ...;
     * PrintStream ps = new PrintStream(os);
     * component.print(ps);</pre>
     * </blockquote>
     * Or print the component to the standard system output (System.out):
     * <blockquote><pre>
     * Component component = ...;
     * component.print(System.out);</pre>
     * </blockquote>
     * Or even print to a file:
     * <blockquote><pre>
     * File destination = new File("/path/to/file.txt");
     * Component component = ...;
     * PrintStream ps = new PrintStream(destination);
     * component.print(ps);</pre>
     * </blockquote>
     * </p>
     * @param stream
     */
    public void print(PrintStream stream) {
        stream.print("[\n");
        print(stream, "");
        stream.print("]\n");
    }

    private void print(PrintStream stream, String x) {
        Class c = this.getClass();
        stream.print(x + "Class = " + c.getName() + "\n");
        for (Field f : c.getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if (f.get(this) instanceof Component) { // append another component
                    if (f.get(this) instanceof Component) {
                        if (this.equals(f.get(this))) {
                            stream.print(x + spaces(3) + "L " + f.getName() + ": <itself>\n");
                        } else {
                            stream.print(x + spaces(3) + "L " + f.getName() + ":\n"
                                    + ((Component) f.get(this)).toString(x + spaces(4)));
                        }
                    }
                } else { // append some non-Component object
                    stream.print(x + spaces(3) + "L "
                            + f.getName() + " = " + f.get(this) + "\n");
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Unexpected condition - Field '" + f.getName() + "' was supposed "
                        + "to be accessible. Method could not access the field!", ex);
            }
        }
    }

    /**
     *
     * <p  align="justify" style="width:60%">
     * A string representation of a Component-type object provides a summary of
     * its contents in terms of its field names and their corresponding values. The
     * summary includes its public, protected as well as private fields.
     * </p>
     * @return
     *      Component content summary.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        return "[\n" + toString("") + "]";
    }

    // TODO: Repeated code! Use print(PrintStream) to implement toString().
    // TODO: No need to have a private method for toString. Use print(PrintStream) instead.
    private String toString(String x) {
        String str = "";
        Class c = this.getClass();
        str += x + "Class = " + c.getName() + "\n";
        for (Field f : c.getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if (f.get(this) instanceof Component) {
                    if (this.equals(f.get(this))) {
                        str += x + spaces(3) + "L " + f.getName() + ": <itself>\n";
                    } else {
                        str += x + spaces(3) + "L " + f.getName() + ":\n"
                                + ((Component) f.get(this)).toString(x + spaces(4));
                    }
                } else {
                    str += x + spaces(3) + "L " + f.getName() + " = " + f.get(this) + "\n";
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Unexpected condition - Field '" + f.getName() + "' was supposed "
                        + "to be accessible. Method could not access the field!", ex);
            }
        }
        return str;
    }

    /**
     * Produces a space (" ") of specified length.
     * @param count
     *      Length of the space sequence
     * @return
     *      Space of given length
     */
    private String spaces(int count) {
        String spaces = "";
        for (int i = 0; i < count; i++) {
            spaces += " ";
        }
        return spaces;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Overrides the method {@link Object#equals(java.lang.Object) equals(Object)} to
     * fit the purposes of equality of two components. Two components are considered to
     * be equal if they possess identical primary key values. A component is not equal to
     * any </code>null</code> object nor is it equal to non-Component objects.
     * </p>
     *
     * @param obj
     *          the reference Component with which to compare.
     * @return
     *          <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     *
     *
     */
    @Override
    public boolean equals(final Object obj) {
        //TODO : Equality when unique fields are identical!
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        boolean areEqual = true;
        for (Field primaryKeyOfThis : getPrimaryKeyFields()) {
            primaryKeyOfThis.setAccessible(true);
            try {
                if (primaryKeyOfThis.get(this) == null) {
                    return primaryKeyOfThis.get(obj) == null ? true : false;
                }
                if (primaryKeyOfThis.get(obj) == null) {
                    return primaryKeyOfThis.get(this) == null ? true : false;
                }
                areEqual = areEqual && primaryKeyOfThis.get(this).equals(primaryKeyOfThis.get(obj));
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return areEqual;
    }

    public List<Field> getPrimaryKeyFields() {
        List<Field> primaryKeyFields = new LinkedList<Field>();
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(PrimaryKey.class) != null) {
                primaryKeyFields.add(fieldOfThis);
            }
        }
        return primaryKeyFields;
    }

    public ArrayList<Field> getForeignKeyFields() {
        ArrayList<Field> foreignKeys = new ArrayList<Field>();
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(ForeignKey.class) != null) {
                foreignKeys.add(fieldOfThis);
            }
        }
        return (ArrayList<Field>)foreignKeys;
    }

    public List<Field> getSelfReferencingFields() {
        List<Field> selfRefForeignKeys = new LinkedList<Field>();
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(ForeignKey.class) != null
                    && fieldOfThis.getType().equals(getClass())) {
                selfRefForeignKeys.add(fieldOfThis);
            }
        }
        return selfRefForeignKeys;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Returns a list of those @{@link org.kinkydesign.decibell.annotations.Entry Entry}
     * fields which are tagged as autogenerated
     * always as identity using the annotation method
     * {@link org.kinkydesign.decibell.annotations.Entry#autoGenerated()  }.
     * </p>
     * @return
     *      Autogenerated field or null if not any.
     */
    public Field getAutogeneratedField() {
        for (Field fieldOfThis : this.getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(org.kinkydesign.decibell.annotations.Entry.class) != null
                    && fieldOfThis.getAnnotation(org.kinkydesign.decibell.annotations.Entry.class).autoGenerated()) {
                return fieldOfThis;
            }
        }
        return null;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Whether the component has a field annotated as primary key. Note that it is
     * mandatory to annotate at least one field as @{@link PrimaryKey } in order to
     * attach it to some instance of {@link DeciBell } and include it in the table
     * creation mechanism of DeciBell&copy;. This method is provided for easy
     * checking.
     * </p>
     * @return
     *      <p  align="justify" style="width:60%">
     *      <code>true</code> if the components contains a primary key.
     *      </p>
     */
    public boolean hasPrimaryKey() {
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(PrimaryKey.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Whether the component contains foreign key annotations
     * </p>
     * @return
     *      <p  align="justify" style="width:60%">
     *      <code>true</code> if the component contains foreign keys.
     *      </p>
     */
    public boolean hasForeignKey() {
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(ForeignKey.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p  align="justify" style="width:60%">
     * Whether the component contains foreign key annotations for fields of the
     * same type as the object holding the field (self-references).
     * </p>
     * @return
     *      <p  align="justify" style="width:60%">
     *      <code>true</code> if the component contains self-referencing fields.
     *      </p>
     */
    public boolean isSelfReferencing() {
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(ForeignKey.class) != null
                    && fieldOfThis.getType().equals(getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
