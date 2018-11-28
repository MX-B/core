package io.gr1d.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * This validator is used to check a limited values inside a string field
 * Example: personType ['INDIVIDUAL', 'COMPANY']
 *
 * @author Sérgio Marcelino
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValueValidator.class})
@Documented
public @interface Value {
    String message() default "{Value.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return Array of allowed values
     */
    String[] values() default {};
}
