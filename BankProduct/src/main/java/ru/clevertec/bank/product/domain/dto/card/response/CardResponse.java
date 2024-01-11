package ru.clevertec.bank.product.domain.dto.card.response;

import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.UUID;

public record CardResponse(
                            String cardNumber,
                            String iban,
                            UUID customerId,
                            CustomerType customerType,
                            String cardholder,
                            CardStatus cardStatus) {
}
