package ru.clevertec.bank.product.domain.dto.deposit;

import java.util.UUID;

public record DepositInfoRequest(UUID customerId,
                                 String customerType,
                                 AccInfoRequest accInfo,
                                 DepInfoRequest depInfo) {
}
