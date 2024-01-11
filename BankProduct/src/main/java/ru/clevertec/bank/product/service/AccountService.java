package ru.clevertec.bank.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.AccountInDto;
import ru.clevertec.bank.product.domain.dto.AccountInDtoRabbit;
import ru.clevertec.bank.product.domain.dto.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.util.AccountMapper;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accRepo;
    private final AccountMapper mapper;
    private final ObjectMapper jacksonMapper;

    public AccountOutDto getAccountByIban(String iban) {
        Account account = accRepo.findById(iban).orElseThrow(() -> new ResourceNotFountException(
                String.format("Account with iban = %s not found", iban)));
        return mapper.toAccountOutDto(account);
    }

    public List<Account> getAccountsByCustomerId(UUID id) {
        return accRepo.findByCustomerId(id);
    }

    public void deleteAccountByIban(String iban) {
        accRepo.deleteById(iban);
    }

    public AccountOutDto createAccount(AccountInDto inDto) {
        Account account = mapper.toAccount(inDto);
        Optional<Account> accountInDb = accRepo.findById(account.getIban());
        if (accountInDb.isPresent()) {
            throw new RequestBodyIncorrectException("Data with such Iban already exist");
        }
        account = accRepo.save(account);
        return mapper.toAccountOutDto(account);
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

    public AccountOutDto saveAccountFromRabbit(String message) {
        try {
            AccountInDtoRabbit rabbitDto = jacksonMapper.readValue(message, AccountInDtoRabbit.class);
            Account account = mapper.toAccount(rabbitDto.getPayload());
            account = accRepo.save(account);
            return mapper.toAccountOutDto(account);
        } catch (JsonProcessingException e) {
            throw new RequestBodyIncorrectException("Data in the request body isn't correct: " + message);
        }
    }



/*    @Transactional
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
    }*/

}
