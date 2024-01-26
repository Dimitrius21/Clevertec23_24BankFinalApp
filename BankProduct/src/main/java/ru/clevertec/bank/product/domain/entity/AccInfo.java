package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Embeddable
public class AccInfo implements Serializable {

    @Column(insertable = false, updatable = false)
    private String accIban;
    private LocalDate accOpenDate;
    private BigDecimal currAmount;
    private String currAmountCurrency;

}
