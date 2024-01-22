package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInAccount implements GetUuid {

    private final AccountService service;

    @Override
    public UUID get(String key) {
        return service.getAccountByIban(key).getCustomerId();
    }

}
