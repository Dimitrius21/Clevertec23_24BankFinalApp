package ru.clevertec.bank.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoResponse;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoResponse;
import ru.clevertec.bank.product.domain.entity.Deposit;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper
public interface DepositMapper {

    String DEPOSIT = "Депозитный";

    @Mapping(target = "accInfo.id", source = "account.id")
    @Mapping(target = "accInfo.accIban", source = "account.iban")
    @Mapping(target = "accInfo.accOpenDate", source = "account.openDate")
    @Mapping(target = "accInfo.currAmount", source = "account.amount")
    @Mapping(target = "accInfo.currencyCode", source = "account.currencyCode")
    @Mapping(target = "accInfo.rate", source = "account.rate")
    @Mapping(target = "depInfo", source = "deposit")
    DepositInfoResponse toDepositInfoResponse(Deposit deposit);

    DepInfoResponse toDepInfoResponse(Deposit deposit);

    @Mapping(target = "account.name", constant = DEPOSIT)
    @Mapping(target = "account.iban", source = "accInfo.accIban")
    @Mapping(target = "account.ibanReadable", source = "accInfo.accIban", qualifiedByName = "formatIban")
    @Mapping(target = "account.amount", source = "accInfo.currAmount")
    @Mapping(target = "account.currencyCode", source = "accInfo.currencyCode")
    @Mapping(target = "account.openDate", expression = "java(LocalDate.now())")
    @Mapping(target = "account.customerId", source = "customerId")
    @Mapping(target = "account.customerType", source = "customerType")
    @Mapping(target = "account.rate", source = "accInfo.rate")
    @Mapping(target = "termVal", source = "depInfo.termVal")
    @Mapping(target = "termScale", source = "depInfo.termScale")
    @Mapping(target = "expDate", source = "depInfo", qualifiedByName = "calculateExpDate")
    @Mapping(target = "depType", source = "depInfo.depType")
    @Mapping(target = "autoRenew", source = "depInfo.autoRenew")
    Deposit toDeposit(DepositInfoRequest request);

    @Mapping(target = "account.id", source = "accId")
    @Mapping(target = "expDate", source = "request", qualifiedByName = "calculateExpDate")
    Deposit toDeposit(Long accId, DepInfoRequest request);

    void updateDeposit(DepInfoUpdateRequest request, @MappingTarget Deposit deposit);

    @Named("formatIban")
    default String formatIban(String iban) {
        return IntStream.range(0, iban.length() / 4)
                .mapToObj(i -> iban.substring(i * 4, i * 4 + 4))
                .collect(Collectors.joining(" "));
    }

    @Named("calculateExpDate")
    default LocalDate calculateExpDate(DepInfoRequest depInfo) {
        Integer termVal = depInfo.termVal();
        String termScale = depInfo.termScale();
        LocalDate openDate = LocalDate.now();
        return switch (termScale) {
            case "D" -> openDate.plusDays(termVal);
            case "M" -> openDate.plusMonths(termVal);
            default -> throw new NotValidRequestParametersException("Invalid termScale: %s".formatted(termScale));
        };
    }

}
