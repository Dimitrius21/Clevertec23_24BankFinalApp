package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.CardService;
import ru.clevertec.bank.product.service.DepositService;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInDeposit implements GetUuid {

    private final DepositService service;

    @Override
    public UUID get(String key) {
        try {
            return service.findByIban(key).customerId();
        } catch (ResourceNotFountException ex) {
            return null;
        }
    }
}
