package ru.clevertec.bank.product.domain.dto.deposit.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.clevertec.bank.product.util.CustomerType;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DepositRabbitPayloadRequest(String customerId,
                                          CustomerType customerType,
                                          RabbitAccInfoRequest accInfo,
                                          RabbitDepInfoRequest depInfo) {
}
