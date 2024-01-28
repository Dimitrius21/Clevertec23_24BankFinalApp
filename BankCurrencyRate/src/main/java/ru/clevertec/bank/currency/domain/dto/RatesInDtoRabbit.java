package ru.clevertec.bank.currency.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class RatesInDtoRabbit {
    private Map<String, String> header;
    private RatesInDto payload;
}
