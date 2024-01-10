package ru.clevertec.bank.product.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.bank.product.domain.dto.AccountInDto;
import ru.clevertec.bank.product.domain.dto.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;

import java.util.Currency;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountOutDto toAccountOutDto(Account account);
    @Mapping(target = "currencyCode", expression = "java(AccountMapper.getCurrencyNameByNumCode(dto.getCurrencyCode()))")
    @Mapping(target = "amount", expression = "java(dto.getAmount().multiply(java.math.BigDecimal.valueOf(100L)).longValueExact())")
    Account toAccount(AccountInDto dto) throws IllegalArgumentException;

    static String getCurrencyNameByNumCode(int code) {
        Currency curr = Currency.getAvailableCurrencies()
                .stream()
                .filter(c -> c.getNumericCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
        return curr.getCurrencyCode();
    }

}
