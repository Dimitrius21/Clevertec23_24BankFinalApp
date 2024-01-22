package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.CreditService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInCredit implements GetUuid {

    private final CreditService service;

    @Override
    public UUID get(String key) {
        return service.findByContractNumber(key).getCustomerId();
    }

}
