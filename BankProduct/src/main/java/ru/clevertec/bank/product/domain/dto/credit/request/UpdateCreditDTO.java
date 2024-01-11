package ru.clevertec.bank.product.domain.dto.credit.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCreditDTO {

    private LocalDate repaymentDate;
    private Double rate;
    private Boolean possibleRepayment;
    private Boolean isClosed;

}
