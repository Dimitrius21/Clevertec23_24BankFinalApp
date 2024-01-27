package ru.clevertec.bank.product.secure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.mapper.AccountMapper;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUuidInAccountTest {

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Mock
    private AccountService accountService;

    @InjectMocks
    private GetUuidInAccount getUuidInAccount;


    @Test
    void getTest() {
        String iban = "";
        Account account = getAccount(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);
        when(accountService.getAccountByIban(iban)).thenReturn(outDto);

        UUID res = getUuidInAccount.get(iban);

        Assertions.assertThat(res).isEqualTo(account.getCustomerId());
    }

    private Account getAccount(String iban) {
        return new Account(iban, "Main", 10000, "BYN",
                LocalDate.of(2024, 1, 10), true, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                CustomerType.LEGAL, 0.0, null);
    }
}