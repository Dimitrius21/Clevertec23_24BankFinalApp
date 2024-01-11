package ru.clevertec.bank.product.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountChangeSumDto {
    private String iban;
    private long change;
    private int currencyCode;
}
