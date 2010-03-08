package org.kinkyDesign.decibell.core.interfaces;

import java.util.Properties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JBuilder {

    /**
     * Attach a new compoent to the builder. This command will not affect the database,
     * once you invoke {@link JBuilder#build() }.
     * @param component the component to be attached to the builder.
     */
    void attach(Class<? extends JComponent> component);
    
    /**
     * Removes a component from the builder.
     * @param component
     */
    void detach(Class<? extends JComponent> component);


    /**
     * Initializes the database.
     */
    void build();

    /**
     * Set properties related to the builder such as the database user and the
     * password for security reasons (protection of the data in the database).
     * @param properties
     */
    void setProperties(Properties properties);
    

}
