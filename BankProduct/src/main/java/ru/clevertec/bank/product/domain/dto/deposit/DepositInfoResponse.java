package ru.clevertec.bank.product.domain.dto.deposit;

public record DepositInfoResponse(//TODO customer dto must be here
                                  AccInfoResponse accInfo,
                                  DepInfoResponse depInfo) {
}
