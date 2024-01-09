package ru.clevertec.bank.product.domain.dto.deposit.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DepInfoRequest(

        @Positive
        BigDecimal rate,

        @Positive
        Integer termVal,

        @Pattern(regexp = "[DM]", message = "Acceptable termScales are only: D(days) or M(months)")
        String termScale,

        @Pattern(regexp = "REVOCABLE|IRREVOCABLE", message = "Acceptable depTypes are only: REVOCABLE or IRREVOCABLE")
        String depType,

        @NotNull
        Boolean autoRenew) {
}
