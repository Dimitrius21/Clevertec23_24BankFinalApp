package ru.clevertec.bank.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.bank.product.domain.dto.account.response.AccountFullOutDto;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDtoRabbit;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.mapper.AccountMapper;
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

    @Transactional(readOnly = true)
    public AccountOutDto getAccountByIban(String iban) {
        return accRepo.findById(iban)
                .map(mapper::toAccountOutDto)
                .orElseThrow(() -> new ResourceNotFountException(String.format("Account with iban = %s not found", iban)));
    }

    @Transactional(readOnly = true)
    public List<AccountFullOutDto> getAllAccounts(Pageable pageable) {
        return accRepo.findAll(pageable).toList()
                .stream()
                .map(mapper::toAccountFullOutDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOutDto> getAccountsByCustomerId(UUID id) {
        return accRepo.findByCustomerId(id).stream()
                .map(mapper::toAccountOutDto)
                .toList();
    }

    @Transactional
    public void deleteAccountByIban(String iban) {
        accRepo.deleteById(iban);
    }

    @Transactional
    public AccountOutDto createAccount(AccountInDto inDto) {
        Account account = mapper.toAccount(inDto);
        Optional<Account> accountInDb = accRepo.findById(account.getIban());
        if (accountInDb.isPresent()) {
            throw new RequestBodyIncorrectException("Data with such Iban already exist");
        }
        return mapper.toAccountOutDto(accRepo.save(account));
    }

    @Transactional
    public AccountOutDto updateAccount(AccountInDto inDto) {
        Account acc = mapper.toAccount(inDto);
        Account accountInDb = accRepo.findById(acc.getIban()).orElseThrow(() -> new ResourceNotFountException(
                String.format("Account with IBAN=%s not found", acc.getIban())));
        if (!accountInDb.getCustomerId().equals(acc.getCustomerId())) {
            throw new RequestBodyIncorrectException("Incorrect customer_id in request");
        }
        accountInDb.setName(acc.getName());
        accountInDb.setMainAcc(acc.isMainAcc());
        accountInDb.setCustomerType(acc.getCustomerType());
        return mapper.toAccountOutDto(accRepo.save(accountInDb));
    }

    @Transactional
    public AccountOutDto saveAccountFromRabbit(String message) {
        try {
            AccountInDtoRabbit rabbitDto = jacksonMapper.readValue(message, AccountInDtoRabbit.class);
            Account account = mapper.toAccount(rabbitDto.getPayload());
            return mapper.toAccountOutDto(accRepo.save(account));
        } catch (JsonProcessingException e) {
            throw new RequestBodyIncorrectException("Data in the request body isn't correct: " + message);
        }
    }

}
