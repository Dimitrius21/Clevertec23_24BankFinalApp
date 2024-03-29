package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.clevertec.bank.product.util.DepositType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Embeddable
public class DepInfo implements Serializable {

    private BigDecimal rate;
    private Integer termVal;
    private Character termScale;
    private LocalDate expDate;

    @Enumerated(EnumType.STRING)
    private DepositType depType;

    private Boolean autoRenew;

}
