package io.gr1d.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This validator is used to validate CPF or CNPJ provided by
 * another field with `document_type`
 *
 * @author Sérgio Marcelino
 */
@Constraint(validatedBy = CPFCNPJValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CPFCNPJ {
    String message() default "{CPFCNPJ.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}