package ru.clevertec.bank.product.domain.dto.deposit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record DepInfoRequest(

        @Positive
        @NotNull
        Integer termVal,

        @Pattern(regexp = "[DM]", message = "Acceptable termScales are only: D(days) or M(months)")
        String termScale,

        @Pattern(regexp = "REVOCABLE|IRREVOCABLE", message = "Acceptable depTypes are only: REVOCABLE or IRREVOCABLE")
        String depType,

        @NotNull
        Boolean autoRenew) {
}
