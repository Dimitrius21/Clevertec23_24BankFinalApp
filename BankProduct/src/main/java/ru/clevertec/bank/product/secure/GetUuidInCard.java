package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.CardService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInCard implements GetUuid {

    private final CardService service;

    @Override
    public UUID get(String key) {
        return service.findById(key).getCustomerId();
    }

}
