package ru.clevertec.bank.product.domain.dto.credit.request;

import lombok.Data;
import ru.clevertec.bank.product.domain.dto.HeaderRequest;

@Data
public class CreditRabbitRequestDTO {
    private HeaderRequest header;
    private CreateCreditDTO payload;
}
