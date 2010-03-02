package org.kinkyDesign.decibell.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any key for a DB table.
 * @author chung
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
public @interface Key {

    boolean unique() default false;
    boolean notNull() default false;
    OnModification onDelete() default OnModification.CASCADE;
    OnModification onUpdate() default OnModification.CASCADE;
}
