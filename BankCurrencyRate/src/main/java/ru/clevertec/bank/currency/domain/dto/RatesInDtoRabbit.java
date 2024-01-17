package ru.clevertec.bank.currency.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.currency.domain.entity.Rate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class RatesInDtoRabbit {
    private Map<String, String> header;
    private RatesInDto payload;
}
