package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class AccInfo {

    private String accIban;
    private LocalDate accOpenDate;
    private BigDecimal currAmount;
    private String currAmountCurrency;

}
