package ru.clevertec.bank.product.domain.dto.deposit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DepInfoUpdateRequest(

        @Pattern(regexp = "REVOCABLE|IRREVOCABLE", message = "Acceptable depTypes are only: REVOCABLE or IRREVOCABLE")
        String depType,

        @NotNull
        Boolean autoRenew) {
}
