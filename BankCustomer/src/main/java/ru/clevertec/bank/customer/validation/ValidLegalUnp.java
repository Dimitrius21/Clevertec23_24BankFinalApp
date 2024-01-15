package ru.clevertec.bank.customer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LegalUnpValidator.class)
public @interface ValidLegalUnp {

    String message() default "Wrong unp! It should be present for LEGAL and absent for PHYSIC";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
