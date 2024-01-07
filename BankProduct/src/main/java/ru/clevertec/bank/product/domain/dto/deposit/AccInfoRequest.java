package ru.clevertec.bank.product.domain.dto.deposit;

public record AccInfoRequest(String accIban,
                             Long currAmount,
                             Integer currencyCode /*TODO change for currency 'BYN'*/) {
}
