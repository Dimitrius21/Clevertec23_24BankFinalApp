package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accRepo;

    public Account getAccountById(long id) {
        Account account = accRepo.findById(id).orElseThrow(() -> new ResourceNotFountException(
                String.format("Account with id=%d not found", id)));
        return account;
    }

    public Account getAccountByClientId(UUID id) {
        Account account = accRepo.findByCustomerId(id).orElseThrow(() -> new ResourceNotFountException(
                String.format("Account for client with id=%s not found", id.toString())));
        return account;
    }

    public void deleteAccountById(long id){
        accRepo.deleteById(id);
    }

    public Account saveAccount(Account acc){
        return accRepo.save(acc);
    }

    public Account updateAccount(Account acc){


        return acc;
    }

}
