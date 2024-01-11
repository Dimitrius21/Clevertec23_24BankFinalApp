package ru.clevertec.bank.product.domain.dto.credit.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreditResponseDTO {

    private UUID customerId;
    private String contractNumber;
    private LocalDate contractStartDate;
    private Long totalDebt;
    private Long currentDebt;
    private String currency;
    private LocalDate repaymentDate;
    private Double rate;
    private String iban;
    private Boolean possibleRepayment;
    private Boolean isClosed;
    private String customerType; 

}
