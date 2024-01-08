package ru.clevertec.bank.currency.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rates;

@Mapper(componentModel = "spring")
public interface RatesMapper {
    RatesOutDto toRatesOutDto(Rates rates);
    Rates toRates(RatesInDto dto);
}
