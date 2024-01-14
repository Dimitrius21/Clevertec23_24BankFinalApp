package ru.clevertec.bank.product.domain.dto.credit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCreditDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate repaymentDate;

    private Double rate;

    private Boolean possibleRepayment;

    private Boolean isClosed;

}
