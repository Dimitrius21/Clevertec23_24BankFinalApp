package ru.clevertec.bank.product.domain.dto.card.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardRabbitPayloadRequest(String cardNumber,
                                       String cardNumberReadable,
                                       String iban,
                                       String customerId,
                                       CustomerType customerType,
                                       String cardholder,
                                       CardStatus cardStatus) {
}
