package ru.clevertec.bank.product.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountOutDto {
        private long id;
        private String name;
        private String iban;
        private String ibanReadable;
        private long amount;
        private int currencyCode;
        private LocalDate openDate;
        private boolean mainAcc;
        private UUID customerId;
        private CustomerType customerType;
        private double rate;
}
