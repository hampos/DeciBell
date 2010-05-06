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
     * Registers the component in the specified database.
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
     * @see RegistrationEngine
     */
    public void register(DeciBell db) throws DuplicateKeyException, ImproperRegistration {
        RegistrationEngine engine = new RegistrationEngine(db);
        engine.register(this);
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
<<<<<<< Updated upstream
        SearchEngine<T> engine = new SearchEngine<T>(db);
        return engine.search(this);
=======
        return search(db, new Component[PENETRATION]);
    }

    private ArrayList<T> search(DeciBell db, Component[] tempComponent) {
        ArrayList<T> resultList = new ArrayList<T>();
        Class c = this.getClass();
        ComponentRegistry registry = ComponentRegistry.getRegistry(db.getDbConnector());
        Table table = (Table) registry.get(c);
        StatementPool pool = StatementPool.getPool(db.getDbConnector());
        Entry<PreparedStatement, SQLQuery> entry = pool.getSearch(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery query = entry.getValue();
        try {
            int i = 1;
            for (Proposition p : query.getPropositions()) {
                JTableColumn col = p.getTableColumn();
                Field field = col.getField();
                field.setAccessible(true);
                Object obj = null;
                try {
                    obj = field.get(this);
                    if (col.isForeignKey()) {
                        Field f = col.getReferenceColumn().getField();
                        f.setAccessible(true);
                        ps.setObject(i, (Object) f.get(obj), col.getColumnType().getType());
                    } else if (obj == null
                            || (col.isTypeNumeric() && ((Double.parseDouble(obj.toString())) == Double.parseDouble(col.getNumericNull())))) {
                        Infinity inf = new Infinity(db.getDbConnector());
                        ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                    } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        ps.setObject(i, obj, col.getColumnType().getType());
                    } else if (!col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        ps.setObject(i, obj, col.getColumnType().getType());
                    } else {
                        XStream xstream = new XStream();
                        String xml = xstream.toXML(obj);
                        ps.setString(i, xml);
                    }
                } catch (NullPointerException ex) {
                    Infinity inf = new Infinity(db.getDbConnector());
                    ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                }
                i++;
            }
            ResultSet rs = ps.executeQuery();

            Constructor constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            while (rs.next() != false) {
                Object newObj = constructor.newInstance();
                for (JTableColumn col : table.getTableColumns()) {
                    Field f = col.getField();
                    f.setAccessible(true);
                    if (col.isForeignKey()) {
                        continue;
                    } else if (col.getColumnType().equals(SQLType.LONG_VARCHAR)) {
                        XStream xstream = new XStream();
                        Object xobj = null;
                        try {
                            xobj = xstream.fromXML((String) rs.getObject(col.getColumnName()));
                        } catch (NullPointerException ex) {
                            xobj = null;
                        }
                        f.set(newObj, xobj);
                    } else {
                        f.set(newObj, rs.getObject(col.getColumnName()));
                    }
                }
                pool.recycleSearch(entry, table);

                for (Set<JTableColumn> group : table.getForeignColumnsByGroup()) {
                    /*
                     * Retrieve the object of the SAME type which is referenced by a
                     * self-referencing foreign column.
                     */
                    Class refClass = group.iterator().next().getReferencesClass();
                    Constructor refConstructor = refClass.getDeclaredConstructor();
                    refConstructor.setAccessible(true);
                    Object refObj = refConstructor.newInstance();
                    for (JTableColumn col : group) {
                        Field f = col.getReferenceColumn().getField();
                        f.setAccessible(true);
                        f.set(refObj, rs.getObject(col.getColumnName()));
                    }
                    Component component = (Component) refObj;


                    if (component.getClass().equals(this.getClass()) && !component.equals(this)
                            || !component.getClass().equals(this.getClass())) {
                        ArrayList tempList = component.search(db);
                        if (tempList.size() > 1) {
                            throw new RuntimeException("Single foreign object list has size > 1");
                        } else if (tempList.size() == 1) {
                            Field f = group.iterator().next().getField();
                            f.setAccessible(true);
                            f.set(newObj, tempList.get(0));
                        }
                    } else if (component.getClass().equals(this.getClass()) && component.equals(this)) {
                        /*
                         * A boolean flag which specifies whether the search is complete or
                         * if we need to search into other objects as well.
                         */
                        boolean bin = false;

                        /*
                         * Examine whether the search should continue the tree spanning
                         * or quit here. This is parametrized by means of the integer PENETRATION.
                         */
                        if (hasNullElement(tempComponent)) {
                            bin = true;
                        } else {
                            for (Component compo : tempComponent) {
                                bin = bin || (!component.equals(compo));
                            }
                        }

                        if (bin) {
                            for (int k = 0; k < PENETRATION - 1; k++) {
                                tempComponent[k] = tempComponent[k + 1];
                            }
                            tempComponent[PENETRATION - 1] = component;
                            ArrayList tempList = component.search(db, tempComponent);
                            if (tempList.size() > 1) {
                                throw new RuntimeException("Single foreign object list has size > 1");
                            } else if (tempList.size() == 1) {
                                Field f = group.iterator().next().getField();
                                f.setAccessible(true);
                                f.set(newObj, tempList.get(0));
                                //System.out.println("setting " + f.getName() + " = " + tempList.get(0));
                            }
                        }
                        /*
                         * Well...we need to study this case a little!
                         */
                    }

                }


                /**
                 * Perform search into the relational tables.
                 */
                for (JRelationalTable relTable : table.getRelations()) {
                    ArrayList relList = new ArrayList();
                    Entry<PreparedStatement, SQLQuery> fentry = pool.getSearch(relTable);
                    ps = fentry.getKey();
                    query = fentry.getValue();
                    Field field = relTable.getOnField();
                    field.setAccessible(true);
                    i = 1;
                    for (Proposition p : query.getPropositions()) {
                        JTableColumn col = p.getTableColumn();
                        if (col.getColumnName().equals("METACOLUMN")) {
                            continue;
                        }
                        Field f = col.getField();
                        f.setAccessible(true);
                        try {
                            if (!col.getReferenceTable().equals(relTable.getMasterTable())) {
                                Infinity inf = new Infinity(db.getDbConnector());
                                ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                            } else {
                                Object obj = f.get(newObj);
                                //TODO: check if NumericNull must be checked here.
                                if (obj == null) {
                                    Infinity inf = new Infinity(db.getDbConnector());
                                    ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                                }
                                ps.setObject(i, obj, col.getColumnType().getType());
                            }
                        } catch (NullPointerException ex) {
                            Infinity inf = new Infinity(db.getDbConnector());
                            ps.setObject(i, inf.getInfinity(p), col.getColumnType().getType());
                        }
                        i++;
                    }
                    ResultSet relRs = ps.executeQuery();

                    String collectionJavaType = null;
                    while (relRs.next() != false) {
                        Class fclass = relTable.getSlaveColumns().iterator().next().
                                getField().getDeclaringClass();
                        //TODO: Maybe declared Constructor is the way to go?
                        Constructor fconstuctor = fclass.getConstructor();
                        fconstuctor.setAccessible(true);
                        Object fobj = fconstuctor.newInstance();
                        for (JTableColumn col : relTable.getSlaveColumns()) {
                            Field ffield = col.getField();
                            ffield.setAccessible(true);
                            ffield.set(fobj, relRs.getObject(col.getColumnName()));
                        }
                        Component component = (Component) fobj;
                        ArrayList tempList = component.search(db);
                        if (tempList.isEmpty()) {
                            throw new RuntimeException("Empty list on search for foreign objects");
                        } else if (tempList.size() > 1) {
                            throw new RuntimeException("Single foreign object list has size > 1");
                        }
                        relList.addAll(tempList);
                        collectionJavaType = relRs.getString("METACOLUMN");
                    }
                    pool.recycleSearch(fentry, relTable);

                    Field onField = relTable.getOnField();
//                    Class onClass = onField.getType();
//                    Constructor con = onClass.getConstructor();
                    Class onClass = Class.forName(collectionJavaType);
                    Constructor con = onClass.getConstructor();
                    Object obj = con.newInstance();
                    Collection relCollection = (Collection) obj;
                    relCollection.addAll(relList);
                    onField.set(newObj, relCollection);
//
//                    if (TypeMap.isSubClass(onField.getType(), Set.class)) {
//                        Class onClass = onField.getType();
//                        Constructor con = onClass.getConstructor();
//                        Object obj = con.newInstance();
//                        Set relSet = (Set)obj;
//                        relSet.addAll(relList);
//                        onField.set(newObj, relSet);
//                    } else {
//                        Class onClass = onField.getType();
//                        Constructor con = onClass.getConstructor();
//                        Object obj = con.newInstance();
//                        List list = (List)obj;
//                        list.addAll(relList);
//                        onField.set(newObj, list);
//                    }
                }

                resultList.add((T) fixSelfReferences((Component) newObj));
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        return resultList;
>>>>>>> Stashed changes
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

    public List<Field> getForeignKeyFields() {
        List<Field> foreignKey = new LinkedList<Field>();
        for (Field fieldOfThis : getClass().getDeclaredFields()) {
            fieldOfThis.setAccessible(true);
            if (fieldOfThis.getAnnotation(ForeignKey.class) != null) {
                foreignKey.add(fieldOfThis);
            }
        }
        return foreignKey;
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
