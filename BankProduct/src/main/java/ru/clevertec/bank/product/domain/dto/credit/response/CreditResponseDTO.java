package ru.clevertec.bank.product.domain.dto.credit.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CreditResponseDTO {

    private String contractNumber;

    @JsonProperty("customer_id")
    private UUID customerId;

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

    @JsonProperty("customer_type")
    private CustomerType customerType;

}
