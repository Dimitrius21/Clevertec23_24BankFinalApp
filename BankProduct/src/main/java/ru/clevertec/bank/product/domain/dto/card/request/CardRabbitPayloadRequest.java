package ru.clevertec.bank.product.domain.dto.card.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardRabbitPayloadRequest(String cardNumber,
                                       String cardNumberReadable,
                                       String iban,
                                       UUID customerId,
                                       CustomerType customerType,
                                       String cardholder,
                                       CardStatus cardStatus) {
}
