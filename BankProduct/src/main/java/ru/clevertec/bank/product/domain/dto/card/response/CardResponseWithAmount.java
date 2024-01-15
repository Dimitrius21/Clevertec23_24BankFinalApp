package ru.clevertec.bank.product.domain.dto.card.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.domain.entity.Amount;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseWithAmount {

    private String cardNumber;
    private String iban;
    private UUID customerId;
    private CustomerType customerType;
    private String cardholder;
    private CardStatus cardStatus;
    private List<Amount> amounts;

}