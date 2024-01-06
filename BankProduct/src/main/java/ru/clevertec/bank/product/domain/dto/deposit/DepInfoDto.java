package ru.clevertec.bank.product.domain.dto.deposit;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DepInfoDto(BigDecimal rate,
                         Integer termVal,
                         Character termScale,
                         LocalDate expDate,
                         String depType,
                         Boolean autoRenew) {
}
