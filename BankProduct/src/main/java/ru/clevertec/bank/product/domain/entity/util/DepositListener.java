package ru.clevertec.bank.product.domain.entity.util;

import jakarta.persistence.PrePersist;
import ru.clevertec.bank.product.domain.entity.Deposit;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;

import java.time.LocalDate;
import java.util.Objects;

public class DepositListener {

    @PrePersist
    public void prePersist(Deposit deposit) {
        if (Objects.isNull(deposit.getAccInfo().getAccOpenDate())) {
            Integer termVal = deposit.getDepInfo().getTermVal();
            Character termScale = deposit.getDepInfo().getTermScale();
            LocalDate openDate = LocalDate.now();
            LocalDate expDate = switch (termScale) {
                case 'D' -> openDate.plusDays(termVal);
                case 'M' -> openDate.plusMonths(termVal);
                default -> throw new NotValidRequestParametersException("Invalid termScale: %s".formatted(termScale));
            };
            deposit.getAccInfo().setAccOpenDate(openDate);
            deposit.getDepInfo().setExpDate(expDate);
        }
    }

}
