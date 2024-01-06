package ru.clevertec.bank.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.AccountChangeSumDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.List;
import java.util.Optional;
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

    public List<Account> getAccountsByCustomerId(UUID id) {
        return accRepo.findByCustomerId(id);
    }

    public void deleteAccountById(long id) {
        accRepo.deleteById(id);
    }

    public Account saveAccount(Account acc) {
        return accRepo.save(acc);
    }

    public Account updateAccountName(Account acc) {
        Account account = accRepo.findByIban(acc.getIban()).orElseThrow(() -> new ResourceNotFountException(
                String.format("Account with IBAN=%s not found", acc.getIban())));
        if (!account.getCustomerId().equals(acc.getCustomerId())) {
            throw new RuntimeException("Incorrect customer");
        }
        account.setName(acc.getName());
        accRepo.save(account);
        return account;
    }

    @Transactional
    public Account updateAccountSum(AccountChangeSumDto acc) {
        Account account = accRepo.findByIban(acc.getIban()).orElseThrow(() -> new ResourceNotFountException(
                String.format("Account with IBAN=%s not found", acc.getIban())));
        if (acc.getCurrencyCode() != account.getCurrencyCode()) {
            throw new RuntimeException("Incorrect currency");
        }
        long newSum = account.getAmount() + acc.getChange();
        if (newSum < 0) {
            throw new RuntimeException("There isn't enough many");
        }
        account.setAmount(newSum);
        accRepo.save(account);
        return account;
    }

}
