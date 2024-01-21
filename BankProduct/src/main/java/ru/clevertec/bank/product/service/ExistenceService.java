package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.repository.CardRepository;
import ru.clevertec.bank.product.repository.CreditRepository;
import ru.clevertec.bank.product.repository.DepositRepository;
import ru.clevertec.bank.product.util.BankProductType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExistenceService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final CreditRepository creditRepository;
    private final DepositRepository depositRepository;

    public boolean existsByCustomerId(UUID id, BankProductType bankProductType) {
        return switch (bankProductType) {
            case ACCOUNT -> accountRepository.existsByCustomerId(id);
            case CARD -> cardRepository.existsByCustomerId(id);
            case CREDIT -> creditRepository.existsByCustomerId(id);
            case DEPOSIT -> depositRepository.existsByCustomerId(id);
            case WRONG -> false;
        };
    }

}
