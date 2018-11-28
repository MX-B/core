package io.gr1d.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This validator is used to compare two dates and validate if the start
 * date is less than or equal to the end date.
 *
 * @author Sérgio Marcelino
 */
@Constraint(validatedBy = DateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {

    /**
     * @return The property name with start date
     */
    String startProperty();

    /**
     * @return The property name with end date
     */
    String endProperty();

    String message() default "DateRange";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}