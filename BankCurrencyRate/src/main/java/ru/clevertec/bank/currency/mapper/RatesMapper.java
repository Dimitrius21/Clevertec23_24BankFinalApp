package ru.clevertec.bank.currency.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rates;

@Mapper
public interface RatesMapper {
    RatesOutDto toRatesOutDto(Rates rates);

    @Mapping(target = "start", source = "startDt")
    Rates toRates(RatesInDto dto);

    @AfterMapping
    default void populateAdditionalParams(@MappingTarget Rates target) {
        target.getExchangeRates().forEach(it -> it.setId(0L));
    }

}
