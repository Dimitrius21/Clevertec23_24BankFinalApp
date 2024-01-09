package ru.clevertec.bank.product.domain.dto.deposit.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.clevertec.bank.product.domain.dto.HeaderRequest;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DepositRabbitInfoRequest(HeaderRequest header,
                                       DepositRabbitPayloadRequest payload) {
}
