package ru.clevertec.bank.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoDto;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.domain.entity.Deposit;

@Mapper
public interface DepositMapper {

    @Mapping(target = "accInfo.accIban", source = "account.iban")
    @Mapping(target = "accInfo.accOpenDate", source = "account.openDate")
    @Mapping(target = "accInfo.currAmount", source = "account.amount")
    @Mapping(target = "accInfo.currencyCode", source = "account.currencyCode")
    @Mapping(target = "depInfo", source = "deposit")
    DepositInfoDto toDepositInfoDto(Deposit deposit);

    @Mapping(target = "account.id", source = "account.id")
    @Mapping(target = "account.name", source = "account.name")
    @Mapping(target = "account.iban", source = "account.iban")
    @Mapping(target = "account.ibanReadable", source = "account.ibanReadable")
    @Mapping(target = "account.amount", source = "account.amount")
    @Mapping(target = "account.currencyCode", source = "account.currencyCode")
    @Mapping(target = "account.openDate", source = "account.openDate")
    @Mapping(target = "account.mainAcc", source = "account.mainAcc")
    @Mapping(target = "account.customerId", source = "account.customerId")
    @Mapping(target = "account.customerType", source = "account.customerType")
    @Mapping(target = "account.rate", source = "account.rate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rate", source = "depInfoDto.rate")
    Deposit toDeposit(DepInfoDto depInfoDto, Account account);

}
