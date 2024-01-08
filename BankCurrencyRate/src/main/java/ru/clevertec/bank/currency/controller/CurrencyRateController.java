package ru.clevertec.bank.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rates;
import ru.clevertec.bank.currency.mapper.RatesMapper;
import ru.clevertec.bank.currency.service.CurrencyRateService;

@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final CurrencyRateService ratesService;
    private final RatesMapper mapper;

    @GetMapping
    public ResponseEntity<RatesOutDto> getLast(){
        Rates rates = ratesService.getLastRates();
        RatesOutDto outDto = mapper.toRatesOutDto(rates);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RatesOutDto> save(@RequestBody RatesInDto inDto){
        Rates rates = mapper.toRates(inDto);
        rates = ratesService.saveRates(rates);
        RatesOutDto outDto = mapper.toRatesOutDto(rates);
        return new ResponseEntity<>(outDto, HttpStatus.CREATED);
    }

}
