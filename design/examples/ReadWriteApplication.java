package examples;

import java.util.ArrayList;
import org.kinkyDesign.decibell.core.Component;
import org.kinkyDesign.decibell.exceptions.DuplicateKeyException;

public class ReadWriteApplication {


public void writeMethod() throws DuplicateKeyException{


  // 1. Construct a component:
  User u = new User();
            u.setUseName("...");


  /*
   Write the component in the database
   */
  u.register();


  /*
   Let the object u be a prototype:
   */
  ArrayList<Component> list = u.search();
  User current = null;
  for (Component c : list){
    current = (User) c;
    System.out.println(current);
  }


}
 
}
