package ru.clevertec.bank.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountFullOutDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.mapper.AccountMapper;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Spy
    private static ObjectMapper jacksonMapper = new ObjectMapper();

    @InjectMocks
    private AccountService accountService;

    @BeforeAll
    public static void setJackson() {
        jacksonMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAccountByIbanTest() {
        String iban = "AABBCCCDDDD0000000000000000";
        Account account = getAccount(iban);
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        AccountOutDto accDto = mapper.toAccountOutDto(account);

        AccountOutDto result = accountService.getAccountByIban(iban);

        Assertions.assertThat(result).isEqualTo(accDto);
    }

    @Test()
    void getAccountByIbanNotFoundTest() {
        String iban = "0000";
        when(accountRepository.findById(iban)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFountException.class, () -> accountService.getAccountByIban(iban));
        Assertions.assertThat(exception.getMessage()).contains(iban);
    }

    @Test
    void getAllAccountsTest() {
        PageRequest pageable = PageRequest.of(0, 2);
        Account account1 = getAccount("AABBCCCDDDD0000000000000000");
        Account account2 = getAccount("AABBCCCDDDD0000000000000001");

        List<Account> newsList = List.of(account1, account2);
        Page<Account> accounts = new PageImpl<>(newsList);
        when(accountRepository.findAll(pageable)).thenReturn(accounts);

        List<AccountFullOutDto> result = accountService.getAllAccounts(pageable);

        List<AccountFullOutDto> accountFullOutDtoList = accounts.stream().map(mapper::toAccountFullOutDto).toList();
        Assertions.assertThat(result).hasSize(2).isEqualTo(accountFullOutDtoList);
    }

    @Test
    void getAccountsByCustomerIdTest() {
        String iban = "AABBCCCDDDD0000000000000000";
        List<Account> accounts = new ArrayList<>();
        accounts.add(getAccount(iban));
        UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");

        when(accountRepository.findByCustomerId(customerId)).thenReturn(accounts);

        List<AccountOutDto> accDto = accounts.stream().map(mapper::toAccountOutDto).toList();

        List<AccountOutDto> result = accountService.getAccountsByCustomerId(customerId);

        Assertions.assertThat(result).isEqualTo(accDto);
    }

    @Test
    void deleteAccountByIbanTest() {
        String iban = "AABBCCCDDDD0000000000000000";

        accountRepository.deleteById(iban);

        verify(accountRepository).deleteById(iban);
    }

    @Test
    void createAccountTest() {
        String iban = "AABBCCCDDDD0000000000000000";
        Account account = getAccount(iban);
        AccountInDto inDto = getInDto(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);

        when(accountRepository.findById(iban)).thenReturn(Optional.empty());
        when(accountRepository.save(account)).thenReturn(account);

        AccountOutDto result = accountService.createAccount(inDto);

        Assertions.assertThat(result).isEqualTo(outDto);
    }

    @Test
    void createAccountFoeExistInDbTest() {
        String iban = "AABBCCCDDDD0000000000000000";
        Account account = getAccount(iban);
        AccountInDto inDto = getInDto(iban);
        when(accountRepository.findById(iban)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(RequestBodyIncorrectException.class, () -> accountService.createAccount(inDto));
        Assertions.assertThat(exception.getMessage()).contains("Data with such Iban already exist");
    }

    @Test
    void updateAccountTest() {
        String iban = "AABBCCCDDDD0000000000000000";
        Account accountInDb = getAccount(iban);

        when(accountRepository.findById(iban)).thenReturn(Optional.of(accountInDb));

        AccountInDto inDto = getInDto(iban);
        Account account = getAccount(iban);
        inDto.setName("MainAccount");
        account.setName("MainAccount");
        inDto.setAmount(BigDecimal.valueOf(300.0));
        AccountOutDto outDto = mapper.toAccountOutDto(account);

        when(accountRepository.save(account)).thenReturn(account);

        AccountOutDto result = accountService.updateAccount(inDto);

        Assertions.assertThat(result).isEqualTo(outDto);
    }

    @Test
    void saveAccountFromRabbitTest() {
        String message = """
                    {
                    "header": { "message_type": "account_info" },
                    "payload": {
                    "name": "Main", "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE", "iban_readable": "AABB CCC DDDD EEEE EEEE EEEE EEEE",
                    "amount": 100.00, "currency_code": "933", "open_date": "10.01.2024", "main_acc": true,
                    "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729", "customer_type" : "LEGAL", "rate": 0.0 }
                }""";
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEEE";
        Account account = getAccount(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);

        when(accountRepository.save(account)).thenReturn(account);

        AccountOutDto result = accountService.saveAccountFromRabbit(message);

        Assertions.assertThat(result).isEqualTo(outDto);
    }

    private Account getAccount(String iban) {
        return new Account(iban, "Main", 10000, "BYN",
                LocalDate.of(2024, 1, 10), true, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                CustomerType.LEGAL, 0.0, null);
    }

    private AccountInDto getInDto(String iban) {
        return new AccountInDto(iban, "Main", iban, BigDecimal.valueOf(100), 933,
                LocalDate.of(2024, 1, 10), true, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                CustomerType.LEGAL, 0.0);
    }

}