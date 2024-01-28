package ru.clevertec.bank.product.domain.dto.deposit.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccInfoResponse(String accIban,

                              @JsonFormat(pattern = "dd.MM.yyyy")
                              LocalDate accOpenDate,

                              BigDecimal currAmount,
                              String currAmountCurrency) implements Serializable {
}
