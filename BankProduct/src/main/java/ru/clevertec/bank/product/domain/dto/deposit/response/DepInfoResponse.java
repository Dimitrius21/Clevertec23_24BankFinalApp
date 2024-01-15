package ru.clevertec.bank.product.domain.dto.deposit.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.clevertec.bank.product.util.DepositType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DepInfoResponse(BigDecimal rate,
                              Integer termVal,
                              Character termScale,

                              @JsonFormat(pattern = "dd.MM.yyyy")
                              LocalDate expDate,

                              DepositType depType,
                              Boolean autoRenew) implements Serializable {
}
