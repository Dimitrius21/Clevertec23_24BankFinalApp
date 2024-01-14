package ru.clevertec.bank.product.domain.dto.credit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateCreditDTO {

    @JsonProperty("customer_id")
    private UUID customerId;

    private String contractNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate contractStartDate;

    private BigDecimal totalDebt;

    private BigDecimal currentDebt;

    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate repaymentDate;

    private Double rate;

    private String iban;

    private Boolean possibleRepayment;

    private Boolean isClosed;

    @Pattern(regexp = "LEGAL|PHYSIC",
            message = "Acceptable customerTypes are only: LEGAL or PHYSIC")
    @JsonProperty("customer_type")
    private String customerType;
}
