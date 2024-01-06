package ru.clevertec.bank.product.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DepositResponse(Long id,
                              AccountInDto account,
                              BigDecimal rate,
                              Integer termVal,
                              Character termScale,
                              LocalDate expDate,
                              String depType,
                              Boolean autoRenew) {
}
