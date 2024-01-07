package ru.clevertec.bank.product.domain.dto.deposit;

import java.time.LocalDate;

public record AccInfoResponse(Long id,
                              String accIban,
                              LocalDate accOpenDate,
                              Long currAmount,
                              Integer currencyCode /*TODO change for currency 'BYN'*/) {
}
