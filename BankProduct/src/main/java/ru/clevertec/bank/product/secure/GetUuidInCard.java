package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.CardService;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInCard implements GetUuid {

    private final CardService service;

    @Override
    public UUID get(String key) {
        try {
            return service.findById(key).getCustomerId();
        } catch (ResourceNotFountException ex) {
            return null;
        }
    }
}
