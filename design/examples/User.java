package org.kinkyDesign.examples;

/*
 Import org.kinkyDesign.decibell classes...
*/
import org.kinkyDesign.decibell.annotations.*;

/* The superclass Component denotes that this class is a corresponds to a database entity
   which is normally a table. This is an example about a User
   The name of the corresponding table in the database is User, i.e. has the same name with the class
   or if there is an abmiguity, the whole name can be used, like org_kinky_example_User  
 */
public class User 
        extends Component {

/*
 Now create some fields for that class...
 */

/* Let the primary key of this class be an integer ID */
@Entry
@PrimaryKey(generatedAsId=false) 
public int ID;

/* 
 When using the annotation @PrimaryKey, it is implied that it is a key, so 
 there is no need to use @Key too. So the above declaration is equivalent to:
 */
@PrimaryKey
public int id;

/*
 Decibell will parse the datatype of the java object (in this case: java.lang.String) and 
 automatically corellate it with a default SQL datatype. The correspondence established is
 String --> VarChar(255)
 Integer, int --> INT
 Long, long --> BigInt
 TimeStamp --> TimeStamp
 Double, Float --> Float
 etc...
 */
@Entry(unique=true, notNull=true) 
public String userName;

/*
 A very simple database entry is generated for the users' ages.
*/
@Entry
public int age;

/*
 A foreign key correspondence is established between the user and the primary key
 of the UserGroup. Note that the class UserGroup is tagged as @Component also! (Otherwise
 an exception is thrown; e.g. IllegalArgumentException or sth like that).
 */
@ForeignKey 
public UserGroup group;

/*
 Establish a K-to-N correspondence between a user and many resources, So this will 
 generate an extra table called user_resources_relations with two columns: one for the 
 primary key of User and one for the primary key of Resource.
 */
@Entry
public ArrayList<Resource> listOfResources;

/*
 Same as above
 */
@Entry
private Resource[] anotherList;

/*
 This is not valid, because the class Object is not tagged as @Component
 */
@Entry
public Object something;

/*
 Add a constraint to a key. This variable takes values from the set {HIGH, LOW}
 */
@Entry
@Constraint(domain={"HIGH", "LOW"}) 
public String kinky;

/*
 This creates a many-to-many relation between this entity (User) and the childName element.
 So this creates a table with two columns: The primary key of User (ID) and another column of
 type VarChar(255).
 */
@Entry
ArrayList<String> childName;


// To be discussed:
// What is the effect of private/protected keywords on the fields
// Can a @Key-like field be private ?
// Introduction of @Constraint(numLow=1, numHigh=100) or
//             @Constraint()


/*
 You can add methods you like without affecting the 
 generation of the table.
 */
public void myMethod(){
}


}/* end of class */
