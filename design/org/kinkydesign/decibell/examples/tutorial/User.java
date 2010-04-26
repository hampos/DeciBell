package org.kinkydesign.decibell.examples.tutorial;

/*
Import org.kinkyDesign.decibell classes...
 */
import java.util.ArrayList;
import org.kinkydesign.decibell.annotations.*;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.Component;


/* The superclass Component denotes that this class is a corresponds to a database entity
which is normally a table. This is an example about a User
The name of the corresponding table in the database is User, i.e. has the same name with the class
or if there is an abmiguity, the whole name can be used, like org_kinky_example_User  
 */
public class User extends Component<User> {

   

    /*
    Now create some fields for that class...
     */

    /* Let the primary key of this class be an integer ID */

    /*
    When using the annotation @PrimaryKey, it is implied that it is a key, so
    there is no need to use @Key too. So the above declaration is equivalent to:
     */
    @PrimaryKey
    private int id=-1;

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
    @Entry(unique = true, notNull = true)
    private String userName;

    /*
    A very simple database entry is generated for the users' ages.
     */
    @Entry(defaultValue="5")
    private int age = -1;

    /*
    A foreign key correspondence is established between the user and the primary key
    of the UserGroup. Note that the class UserGroup is tagged as @Component also! (Otherwise
    an exception is thrown; e.g. IllegalArgumentException or sth like that).
     */
    @ForeignKey
    private UserGroup group = null;

    @ForeignKey
    private ArrayList<UserGroup> groups;

    @Entry
    private Resource resource = null;

    @Entry
    ArrayList<Resource> childName = new ArrayList<Resource>();
    {
        childName.add(new Resource(15, "child1"));
        childName.add(new Resource(16, "child3"));
    }

    public User(){        
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<UserGroup> groups) {
        this.groups = groups;
    }

    
}/* end of class */
