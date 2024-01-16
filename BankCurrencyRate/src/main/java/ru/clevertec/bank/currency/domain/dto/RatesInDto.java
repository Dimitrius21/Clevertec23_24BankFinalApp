package ru.clevertec.bank.currency.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.OffsetTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.currency.domain.entity.Rate;
import ru.clevertec.bank.currency.mapper.DateTimeDeserialize;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatesInDto {
    private long id;
    @JsonDeserialize(using =  DateTimeDeserialize.class)
    private ZonedDateTime startDt;
    private List<Rate> exchangeRates;
}
