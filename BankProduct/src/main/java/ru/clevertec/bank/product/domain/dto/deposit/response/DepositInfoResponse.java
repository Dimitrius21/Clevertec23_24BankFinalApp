package ru.clevertec.bank.product.domain.dto.deposit.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DepositInfoResponse(UUID customerId,
                                  CustomerType customerType,
                                  AccInfoResponse accInfo,
                                  DepInfoResponse depInfo) {
}
