package ru.clevertec.bank.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.clevertec.bank.product.validation.annotation.ValidCurrency;

import java.util.Currency;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
        return Currency.getAvailableCurrencies()
                .stream()
                .map(Currency::getCurrencyCode)
                .anyMatch(code -> code.equals(currency));
    }

}
