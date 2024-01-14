package ru.clevertec.bank.product.domain.dto.credit.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreditResponseDTO {

    @JsonProperty("customer_id")
    private UUID customerId;

    private String contractNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate contractStartDate;

    private Long totalDebt;

    private Long currentDebt;

    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate repaymentDate;

    private Double rate;

    private String iban;

    private Boolean possibleRepayment;

    private Boolean isClosed;

    @JsonProperty("customer_type")
    private String customerType; 

}
