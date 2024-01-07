package ru.clevertec.bank.product.domain.dto.deposit;

import java.time.LocalDate;

public record DepInfoResponse(Long id,
                              Integer termVal,
                              Character termScale,
                              LocalDate expDate,
                              String depType,
                              Boolean autoRenew) {
}
