package ru.clevertec.bank.product.domain.dto.credit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.util.CustomerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditDTO {

    @NotBlank
    private String contractNumber;

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate contractStartDate;

    @Positive
    private BigDecimal totalDebt;

    @Positive
    private BigDecimal currentDebt;

    @NotBlank
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate repaymentDate;

    @PositiveOrZero
    private Double rate;

    @Pattern(regexp = "^[A-Z]{27}$")
    private String iban;

    @NotNull
    private Boolean possibleRepayment;

    @NotNull
    private Boolean isClosed;

    @JsonProperty("customer_type")
    @NotNull
    private CustomerType customerType;
}
