package ru.clevertec.bank.product.domain.dto.deposit;

public record DepInfoRequest(Integer termVal,
                             Character termScale,
                             String depType,
                             Boolean autoRenew) {
}
