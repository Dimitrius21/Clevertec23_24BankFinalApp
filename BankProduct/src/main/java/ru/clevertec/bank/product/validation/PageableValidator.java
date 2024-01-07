package ru.clevertec.bank.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageableValidator implements ConstraintValidator<ValidPageable, Pageable> {

    private Pattern pattern;

    @Override
    public void initialize(ValidPageable constraintAnnotation) {
        pattern = Pattern.compile(constraintAnnotation.regexp());
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        return Optional.ofNullable(pageable)
                .map(Pageable::getSort)
                .map(Streamable::get)
                .map(orders -> orders.map(Sort.Order::getProperty)
                        .map(pattern::matcher)
                        .allMatch(Matcher::matches))
                .orElse(true);
    }

}
