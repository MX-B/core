package io.gr1d.core.validation;

import org.hibernate.validator.constraints.Mod11Check;
import org.hibernate.validator.internal.constraintvalidators.hv.Mod11CheckValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator to fields with CPF or CNPJ allowed.
 *
 * @author Sérgio Marcelino
 */
public class CPFCNPJValidator implements ConstraintValidator<CPFCNPJ, String> {

    private final Mod11CheckValidator cpfWithoutSeparatorMod11Validator1 = new Mod11CheckValidator();
    private final Mod11CheckValidator cpfWithoutSeparatorMod11Validator2 = new Mod11CheckValidator();

    private final Mod11CheckValidator cnpjWithoutSeparatorMod11Validator1 = new Mod11CheckValidator();
    private final Mod11CheckValidator cnpjWithoutSeparatorMod11Validator2 = new Mod11CheckValidator();

    @Override
    public void initialize(final CPFCNPJ constraintAnnotation) {
        this.cpfWithoutSeparatorMod11Validator1.initialize(0, 8, 9, true, 2147483647, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT, new int[0]);
        this.cpfWithoutSeparatorMod11Validator2.initialize(0, 9, 10, true, 2147483647, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT, new int[0]);

        this.cnpjWithoutSeparatorMod11Validator1.initialize(0, 11, 12, true, 9, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT, new int[0]);
        this.cnpjWithoutSeparatorMod11Validator2.initialize(0, 12, 13, true, 9, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT, new int[0]);
    }

    @Override
    public boolean isValid(final String documentNumber, final ConstraintValidatorContext context) {
        if (documentNumber == null) {
            return true;
        }

        if (documentNumber.length() == 11) {
            return this.cpfWithoutSeparatorMod11Validator1.isValid(documentNumber, context)
                    && this.cpfWithoutSeparatorMod11Validator2.isValid(documentNumber, context);
        }
        if (documentNumber.length() == 14) {
            return this.cnpjWithoutSeparatorMod11Validator1.isValid(documentNumber, context)
                    && this.cnpjWithoutSeparatorMod11Validator2.isValid(documentNumber, context);
        }

        return false;
    }
}
