package ru.clevertec.bank.product.util;

import org.mapstruct.Mapper;
import ru.clevertec.bank.product.domain.dto.AccountInDto;
import ru.clevertec.bank.product.domain.dto.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountOutDto toAccountOutDto(Account account);
    Account toAccount(AccountInDto dto);
}
