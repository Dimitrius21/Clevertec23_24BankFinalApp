package ru.clevertec.bank.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.service.CurrencyRateService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final CurrencyRateService ratesService;

    @GetMapping
    public ResponseEntity<RatesOutDto> getCurrent() {
        LocalDateTime now = LocalDateTime.now();
        RatesOutDto outDto = ratesService.getLastRates(now);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @GetMapping("/{time}")
    public ResponseEntity<RatesOutDto> getLast(@PathVariable LocalDateTime time) {
        RatesOutDto outDto = ratesService.getRatesAtTime(time);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RatesOutDto> craete(@RequestBody RatesInDto inDto) {
        RatesOutDto outDto = ratesService.createRates(inDto);
        return new ResponseEntity<>(outDto, HttpStatus.CREATED);
    }

}
