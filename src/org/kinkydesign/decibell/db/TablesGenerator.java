package org.kinkydesign.decibell.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kinkydesign.decibell.annotations.ForeignKey;
import org.kinkydesign.decibell.core.Component;
import org.kinkydesign.decibell.db.interfaces.JDbConnector;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TablesGenerator {
    

    public static void construct(JDbConnector connector, Set<Class<? extends Component>> components){
       for (Class<? extends Component> c : components) {
            try {
                Constructor constr = c.getConstructor();
                Object o = constr.newInstance();
                for (Field f : c.getDeclaredFields()){
                    for (Annotation a : f.getAnnotations()){
                    
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(TablesGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }

    private String tableCreationSQL(Class<? extends Component> c){

        for (Field componentField : c.getDeclaredFields()){
            if (componentField.getAnnotation(ForeignKey.class)!=null) {
                //tableCreationSQL(componentField.)
                break;
            }
        }

        return null;
    }


    
    private static boolean tableExists(JDbConnector connector, String tableName){
      
        return false;
    }

    
    

}