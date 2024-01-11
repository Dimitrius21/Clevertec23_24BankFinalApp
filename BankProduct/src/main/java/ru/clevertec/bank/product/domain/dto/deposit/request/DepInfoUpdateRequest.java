package ru.clevertec.bank.product.domain.dto.deposit.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DepInfoUpdateRequest(

        @Pattern(regexp = "REVOCABLE|IRREVOCABLE", message = "Acceptable depTypes are only: REVOCABLE or IRREVOCABLE")
        String depType,

        @NotNull
        Boolean autoRenew) {
}
