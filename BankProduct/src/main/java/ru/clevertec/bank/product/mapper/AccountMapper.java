package ru.clevertec.bank.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountFullOutDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;

import java.util.Currency;

@Mapper
public interface AccountMapper {
    @Mapping(target = "amount", expression = "java(account.getAmount()/100 + \".\" + account.getAmount()%100)")
    AccountOutDto toAccountOutDto(Account account);

    AccountFullOutDto toAccountFullOutDto(Account account);

    @Mapping(target = "currencyCode", expression = "java(AccountMapper.getCurrencyNameByNumCode(dto.getCurrencyCode()))")
    @Mapping(target = "amount", expression = "java(dto.getAmount().multiply(java.math.BigDecimal.valueOf(100L)).longValueExact())")
    Account toAccount(AccountInDto dto) throws IllegalArgumentException;

    static String getCurrencyNameByNumCode(int code) {
        Currency curr = Currency.getAvailableCurrencies()
                .stream()
                .filter(c -> c.getNumericCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return curr.getCurrencyCode();
    }

}
