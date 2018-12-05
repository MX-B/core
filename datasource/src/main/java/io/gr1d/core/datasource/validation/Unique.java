package io.gr1d.core.datasource.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to validate logical unique constraints
 *
 * @author Sérgio Marcelino
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    String message() default "{io.gr1d.core.datasource.validation.Unique.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    /**
     * The entity to check uniqueness
     */
    Class<?> entity();

    /**
     * The entity's property to check uniqueness
     */
    String property();
}
