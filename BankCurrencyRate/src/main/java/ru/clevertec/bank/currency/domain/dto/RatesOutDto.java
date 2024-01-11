package ru.clevertec.bank.currency.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.currency.domain.entity.Rate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class RatesOutDto {
    private long id;
    private LocalDateTime start;
    private List<Rate> exchangeRates;
}
