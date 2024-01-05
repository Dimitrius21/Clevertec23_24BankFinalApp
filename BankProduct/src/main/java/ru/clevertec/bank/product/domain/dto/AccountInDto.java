package ru.clevertec.bank.product.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountInDto {
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
