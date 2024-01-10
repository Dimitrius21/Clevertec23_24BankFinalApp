package ru.clevertec.bank.currency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rates;
import ru.clevertec.bank.currency.mapper.RatesMapper;
import ru.clevertec.bank.currency.repository.CurrencyRateRepository;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CurrencyRateService {
    private final CurrencyRateRepository rateRepo;
    private final RatesMapper mapper;
    private final ObjectMapper jacksonMapper;

    public RatesOutDto getLastRates(LocalDateTime now){
        Rates rates = rateRepo.findFirstByStartBeforeOrderByStartDesc(now);
        return mapper.toRatesOutDto(rates);
    }

    public RatesOutDto getRatesAtTime(LocalDateTime time){
        Rates rates = rateRepo.findFirstByStartBeforeOrderByStartDesc(time);
        return mapper.toRatesOutDto(rates);
    }

    public RatesOutDto createRates(RatesInDto inDto){
        Rates rates = mapper.toRates(inDto);
        rates = rateRepo.save(rates);
        return mapper.toRatesOutDto(rates);
    }

/*    public Rates saveRatesFromRabbit(String message){
        try {
            RatesInDtoRabbit rabbitDto = jacksonMapper.readValue(message, RatesInDtoRabbit.class);
            Rates rates = mapper.toRates(rabbitDto.getPayload());
            return rateRepo.save(rates);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        *//*        Pageable pageable = PageRequest.of(1,1, Sort.Direction.DESC);
        Page<Rates> page = rateRepo.findAll(pageable);
        List<Rates> ratesList = page.toList();
        if (page.isEmpty()) {
            throw new GeneralException("No rates");
        }
        return page.getContent().get(0);*//*
    }*/

}
