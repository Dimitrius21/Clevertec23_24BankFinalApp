package ru.clevertec.bank.currency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.currency.domain.entity.Rates;
import ru.clevertec.bank.currency.repository.CurrencyRateRepository;
import ru.clevertec.exceptionhandler.exception.GeneralException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CurrencyRateService {
    private final CurrencyRateRepository rateRepo;

    public Rates getLastRates(){
/*        Pageable pageable = PageRequest.of(1,1, Sort.Direction.DESC);
        Page<Rates> page = rateRepo.findAll(pageable);
        List<Rates> ratesList = page.toList();
        if (page.isEmpty()) {
            throw new GeneralException("No rates");
        }
        return page.getContent().get(0);*/
        return rateRepo.findLastRates();
        //return null;
    }

    public Rates saveRates(Rates rates){
        return rateRepo.save(rates);
    }

}
