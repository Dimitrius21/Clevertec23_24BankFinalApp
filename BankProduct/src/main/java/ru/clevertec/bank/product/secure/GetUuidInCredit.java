package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.CardService;
import ru.clevertec.bank.product.service.CreditService;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInCredit implements GetUuid {

    private final CreditService service;

    @Override
    public UUID get(String key) {
        try {
            return service.findByContractNumber(key).getCustomerId();
        } catch (ResourceNotFountException ex) {
            return null;
        }
    }
}
