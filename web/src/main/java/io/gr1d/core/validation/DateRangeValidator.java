package io.gr1d.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * Validator for a range of two dates to check the start date never be
 * greater to end date.
 *
 * @author Sérgio Marcelino
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String message;
    private String startProperty;
    private String endProperty;

    @Override
    public void initialize(final DateRange constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.startProperty = constraintAnnotation.startProperty();
        this.endProperty = constraintAnnotation.endProperty();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }

        final Method start = getGetterForProperty(object.getClass(), startProperty);
        final Method end = getGetterForProperty(object.getClass(), endProperty);

        try {
            final LocalDate startDate = (LocalDate) start.invoke(object);
            final LocalDate endDate = (LocalDate) end.invoke(object);
            if (startDate.compareTo(endDate) > 0) {
                context.buildConstraintViolationWithTemplate("{" + message + ".start}")
                        .addPropertyNode(startProperty)
                        .addConstraintViolation()
                        .buildConstraintViolationWithTemplate("{" + message + ".end}")
                        .addPropertyNode(endProperty)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            return true;
        }

        return true;
    }

    private Method getGetterForProperty(final Class<?> clazz, final String property) {
        final String methodName = "get" + LOWER_CAMEL.to(UPPER_CAMEL, property);
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
