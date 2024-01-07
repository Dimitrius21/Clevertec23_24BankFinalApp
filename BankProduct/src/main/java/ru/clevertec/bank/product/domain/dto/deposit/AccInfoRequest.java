package ru.clevertec.bank.product.domain.dto.deposit;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccInfoRequest(

        @Size(min = 28, max = 28)
        String accIban,

        @Positive
        Long currAmount,

        @Positive
        Integer currencyCode, /*TODO change for currency 'BYN'*/

        @Positive
        BigDecimal rate) {
}
