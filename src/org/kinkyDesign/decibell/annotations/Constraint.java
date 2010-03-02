package org.kinkyDesign.decibell.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Establish a constraint for a key. The constraint can either be numeric or
 * nominal. For example <code>@Constraint(domain={"A", "B", "C"})</code> is a nominal constraint
 * demanding that the underlying variable takes values from the set <code>{A, B, C}</code>. The
 * constraint <code>@Constraint(low=10, high=20)</code> means that the variable is constrained in
 * the closed interval [10,20].
 * 
 * @author Chung
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
public @interface Constraint {

  double low() default Double.MIN_VALUE;
  double high() default Double.MAX_VALUE;
  String[] domain() default {""};

}
