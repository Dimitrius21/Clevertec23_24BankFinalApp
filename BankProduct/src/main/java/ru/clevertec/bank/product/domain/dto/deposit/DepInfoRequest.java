package ru.clevertec.bank.product.domain.dto.deposit;

import java.math.BigDecimal;

public record DepInfoRequest(BigDecimal rate,
                             Integer termVal,
                             Character termScale,
                             String depType,
                             Boolean autoRenew) {
}
