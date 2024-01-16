package ru.clevertec.bank.currency.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesInDtoRabbit;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rates;
import ru.clevertec.bank.currency.mapper.RatesMapper;
import ru.clevertec.bank.currency.repository.CurrencyRateRepository;
import ru.clevertec.exceptionhandler.exception.GeneralException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

import java.time.ZonedDateTime;


@Service
@RequiredArgsConstructor
public class CurrencyRateService {
    private final CurrencyRateRepository rateRepo;
    private final RatesMapper mapper;
    private final ObjectMapper jacksonMapper;

    public RatesOutDto getLastRates(ZonedDateTime now) {
        Rates rates = rateRepo.findFirstByStartBeforeOrderByStartDesc(now);
        return mapper.toRatesOutDto(rates);
    }

    public RatesOutDto createRates(RatesInDto inDto) {
        Rates rates = mapper.toRates(inDto);
        rates = rateRepo.save(rates);
        return mapper.toRatesOutDto(rates);
    }

    public Rates saveRatesFromRabbit(String message) {
        try {
            RatesInDtoRabbit rabbitDto = jacksonMapper.readValue(message, RatesInDtoRabbit.class);
            Rates rates = mapper.toRates(rabbitDto.getPayload());
            return rateRepo.save(rates);
        } catch (JsonProcessingException e) {
            throw new RequestBodyIncorrectException("Data in the request body isn't correct: " + message);
        } catch (Exception e) {
            throw new GeneralException("Error of saving in DB");
        }
    }

}
