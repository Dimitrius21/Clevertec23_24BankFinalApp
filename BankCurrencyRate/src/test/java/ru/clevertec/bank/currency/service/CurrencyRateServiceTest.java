package ru.clevertec.bank.currency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rate;
import ru.clevertec.bank.currency.domain.entity.Rates;
import ru.clevertec.bank.currency.mapper.RatesMapper;
import ru.clevertec.bank.currency.repository.CurrencyRateRepository;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {

    @Mock
    private CurrencyRateRepository repository;

    @Spy
    private RatesMapper mapper = Mappers.getMapper(RatesMapper.class);

    @Spy
    private static ObjectMapper jacksonMapper = new ObjectMapper();

    @InjectMocks
    CurrencyRateService service;

    @Test
    void getLastRates() {
        ZonedDateTime time = ZonedDateTime.now();
        Rates rates = getRates(time, 1);
        rates.setId(1L);
        RatesOutDto dto = mapper.toRatesOutDto(rates);

        ZonedDateTime timeForRequest = ZonedDateTime.now();

        when(repository.findFirstByStartBeforeOrderByStartDesc(timeForRequest)).thenReturn(rates);

        RatesOutDto result = service.getLastRates(timeForRequest);

        Assertions.assertThat(result).isEqualTo(dto);
    }

    @Test
    void createRatesTest() {
        ZonedDateTime time = ZonedDateTime.now();
        Rates rates = getRates(time, 0);
        RatesInDto inDto = getRatesInDto(time, 0);
        Rates ratesFromDb = getRates(time, 1);
        RatesOutDto outDto = mapper.toRatesOutDto(ratesFromDb);

        when(repository.save(rates)).thenReturn(ratesFromDb);

        RatesOutDto result = service.createRates(inDto);

        Assertions.assertThat(result).isEqualTo(outDto);
    }

    @Test
    void saveRatesFromRabbitTest() {
        String message = """
                { "header": { "message_type": "currency-rate" },
                   "payload": {
                           "startDt": "2024-01-03T13:56:51.604498616+03:00",
                           "exchangeRates": [
                              {
                                 "buyRate": 3.44,
                                  "sellRate": 3.45,
                                  "srcCurr": "EUR",
                                  "reqCurr": "BYN"
                              },
                              {
                                  "buyRate": 3.12,
                                  "sellRate": 3.15,
                                  "srcCurr": "USD",
                                  "reqCurr": "BYN"
                              }   
                           ]}
                     }
                     """;
        String value = "2024-01-03T13:56:51.604498616+03:00";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime time = ZonedDateTime.parse(value, formatter);
        Rates rates = getRates(time, 0);
        Rates ratesFromDb = getRates(time, 1);

        when(repository.save(rates)).thenReturn(ratesFromDb);

        Rates result = service.saveRatesFromRabbit(message);

        Assertions.assertThat(result).isEqualTo(ratesFromDb);
    }

    @Test
    void saveRatesFromRabbitWithExceptionTest() {
        String message = """
                { "header": { "message_type": "currency-rate" },
                   "payload": {
                           "startDt": "2024-01-03T13:56:51.604498616+03:00",
                           "exchangeRates": [
                              {
                                 "buyRate": 3.44,
                                  "sellRate": "EUR",
                                  "srcCurr": "EUR",
                                  "reqCurr": "BYN"
                              },
                              {
                                  "buyRate": 3.12,
                                  "sellRate": 3.15,
                                  "srcCurr": "USD",
                                  "reqCurr": "BYN"
                              }   
                           ]}
                     }
                     """;
        Exception exception = assertThrows(RequestBodyIncorrectException.class, ()->service.saveRatesFromRabbit(message));
        Assertions.assertThat(exception.getMessage()).contains("Data in the request body isn't correct:");
    }

    private Rates getRates(ZonedDateTime time, long id) {
        return new Rates(id, time, getRateList(id));
    }

    private RatesInDto getRatesInDto(ZonedDateTime time, long id) {
        return new RatesInDto(id, time, getRateList(id));
    }

    private List<Rate> getRateList(long id) {
        Rate rateEUR = new Rate(id, 3.44, 3.45, "EUR", "BYN");
        Rate rateUSD = new Rate(id, 3.12, 3.15, "USD", "BYN");
        return List.of(rateEUR, rateUSD);
    }
}