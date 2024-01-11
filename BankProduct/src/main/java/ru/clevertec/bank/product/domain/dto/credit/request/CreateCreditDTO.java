package ru.clevertec.bank.product.domain.dto.credit.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateCreditDTO {

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

    @Pattern(regexp = "LEGAL|PHYSIC",
            message = "Acceptable customerTypes are only: LEGAL or PHYSIC")
    private String customerType;

}
