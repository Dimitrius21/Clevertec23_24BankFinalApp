package ru.clevertec.bank.product.domain.dto.deposit;

public record DepositInfoDto(//TODO customer dto must be here
                             AccInfoDto accInfo,
                             DepInfoDto depInfo) {
}
