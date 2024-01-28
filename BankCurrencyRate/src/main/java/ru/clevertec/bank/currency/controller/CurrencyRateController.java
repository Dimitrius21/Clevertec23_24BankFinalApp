package ru.clevertec.bank.currency.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.currency.controller.openapi.CurrencyRateOpenAPI;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.service.CurrencyRateService;
import ru.clevertec.loggingstarter.annotation.Loggable;

import java.time.ZonedDateTime;

@Slf4j
@Loggable
@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class CurrencyRateController implements CurrencyRateOpenAPI {
    private final CurrencyRateService ratesService;

    @GetMapping
    public ResponseEntity<RatesOutDto> getCurrent() {
        ZonedDateTime now = ZonedDateTime.now();
        RatesOutDto outDto = ratesService.getLastRates(now);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @GetMapping("/{time}")
    public ResponseEntity<RatesOutDto> getLastForTime(@PathVariable ZonedDateTime time) {
        RatesOutDto outDto = ratesService.getLastRates(time);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RatesOutDto> create(@RequestBody RatesInDto inDto) {
        RatesOutDto outDto = ratesService.createRates(inDto);
        log.info("Data with rates has been saved id BD");
        return new ResponseEntity<>(outDto, HttpStatus.CREATED);
    }

}
