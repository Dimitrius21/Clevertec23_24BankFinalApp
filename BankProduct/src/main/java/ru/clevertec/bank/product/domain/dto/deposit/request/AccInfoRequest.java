package ru.clevertec.bank.product.domain.dto.deposit.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccInfoRequest(

        @Size(min = 1, max = 28)
        String accIban,

        @Positive
        BigDecimal currAmount,

        String currAmountCurrency) {
}
