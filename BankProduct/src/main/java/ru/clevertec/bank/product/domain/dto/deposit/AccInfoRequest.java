package ru.clevertec.bank.product.domain.dto.deposit;

import java.math.BigDecimal;

public record AccInfoRequest(String accIban,
                             Long currAmount,
                             Integer currencyCode, /*TODO change for currency 'BYN'*/
                             BigDecimal rate) {
}
