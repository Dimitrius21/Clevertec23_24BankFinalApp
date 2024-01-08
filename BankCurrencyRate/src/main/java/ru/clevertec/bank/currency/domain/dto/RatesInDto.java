package ru.clevertec.bank.currency.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.currency.domain.entity.Rate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class RatesInDto {
    private long id;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime start;
    private List<Rate> exchangeRates;
}
