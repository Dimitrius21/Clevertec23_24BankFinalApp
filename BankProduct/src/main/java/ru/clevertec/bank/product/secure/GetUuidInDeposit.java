package ru.clevertec.bank.product.secure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.DepositService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUuidInDeposit implements GetUuid {

    private final DepositService service;

    @Override
    public UUID get(String key) {
        return service.findByIban(key).customerId();
    }

}
