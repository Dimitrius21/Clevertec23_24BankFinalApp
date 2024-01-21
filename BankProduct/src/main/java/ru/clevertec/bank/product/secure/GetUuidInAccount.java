package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.service.CardService;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInAccount implements GetUuid {

    private final AccountService service;

    @Override
    public UUID get(String key) {
        try {
            return service.getAccountByIban(key).getCustomerId();
        } catch (ResourceNotFountException ex) {
            return null;
        }
    }
}
