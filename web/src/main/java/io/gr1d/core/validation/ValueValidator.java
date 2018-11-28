package io.gr1d.core.validation;

import org.apache.commons.lang3.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates a given object's String representation to match one of the provided
 * values.
 *
 * @author Sérgio Marcelino
 */
public class ValueValidator implements ConstraintValidator<Value, Object> {
    /**
     * String array of possible enum values
     */
    private String[] values;

    @Override
    public void initialize(final Value constraintAnnotation) {
        this.values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        return ArrayUtils.contains(this.values, value == null ? null : value.toString());
    }
}
