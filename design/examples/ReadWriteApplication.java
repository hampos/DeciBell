public class WriteReadApplication {


public void writeMethod(){


  // 1. Construct a component:
  User u = new User();
            u.setUseName("...");


  /*
   Write the component in the database
   */
  u.registerIt();


  /*
   Let the object u be a prototype:
   */
  ArrayList<Component> list = r.read();
  User current = null;
  for (Component c : list){
    current = (User) c;
    System.out.println(current.getUserName());
  }


}
 
}
