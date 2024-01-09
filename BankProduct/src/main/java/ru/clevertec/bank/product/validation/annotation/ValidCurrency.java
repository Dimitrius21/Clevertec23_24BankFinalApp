package ru.clevertec.bank.product.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.clevertec.bank.product.validation.CurrencyValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
public @interface ValidCurrency {

    String message() default "Wrong currency! It doesn't exist in java.util.Currency";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
