package ru.clevertec.bank.product.domain.dto.account.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class AccountInDtoRabbit {
    private HashMap<String, String> header;
    private AccountInDto payload;
}
