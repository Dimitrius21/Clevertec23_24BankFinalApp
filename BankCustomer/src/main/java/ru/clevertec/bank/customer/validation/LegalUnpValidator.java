package ru.clevertec.bank.customer.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;

public class LegalUnpValidator implements ConstraintValidator<ValidLegalUnp, CustomerRequest> {

    @Override
    public boolean isValid(CustomerRequest request, ConstraintValidatorContext constraintValidatorContext) {
        String customerType = request.customerType();
        String unp = request.unp();
        return (customerType.equals("LEGAL") && unp != null && !unp.isEmpty())
               || (customerType.equals("PHYSIC") && (unp == null || unp.isEmpty()));
    }

}
