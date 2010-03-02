package org.kinkyDesign.examples;

/*
 Import org.kinkyDesign.decibell classes...
*/
import org.kinkyDesign.decibell.annotations.*;

/* The superclass Component denotes that this class is a corresponds to a database entity
   which is normally a table. This is an example about a User  
 */
public class User 
        extends Component {

/*
 Now create some fields for that class...
 */

/* Let the primary key of this class be an integer ID */
@Key
@PrimaryKey(generatedAsId=false) 
public int ID;

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
@Key(unique=true, notNull=true) 
public String userName;

/*
 A very simple database entry is generated for the users' ages.
*/
@Key 
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
@Key 
public ArrayList<Resource> listOfResources;

/*
 Same as above
 */
@Key 
private Resource[] anotherList;

/*
 This is not valid, because the class Object is not tagged as @Component
 */
@Key 
public Object something;

/*
 Add a constraint to a key. This variable takes values from the set {HIGH, LOW}
 */
@Key 
@Constraint(domain={"HIGH", "LOW"}) 
public String kinky;


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
