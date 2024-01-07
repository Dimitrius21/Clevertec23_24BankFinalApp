package ru.clevertec.bank.product.domain.dto.deposit;

public record DepInfoUpdateRequest(String depType,
                                   Boolean autoRenew) {
}
