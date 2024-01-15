package ru.clevertec.bank.product.domain.dto.deposit.request;

import java.math.BigDecimal;

public record DepositFilterRequest(String accIban,
                                   boolean greaterThan,
                                   BigDecimal amount,
                                   String currency) {
}
