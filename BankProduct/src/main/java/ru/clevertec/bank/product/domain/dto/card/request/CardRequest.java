package ru.clevertec.bank.product.domain.dto.card.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardRequest(

        @Pattern(regexp = "^[0-9]{16}$")
        String cardNumber,

        String cardNumberReadable,

        @Pattern(regexp = "^[A-Z0-9]{27}$")
        String iban,

        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        String customerId,

        @Pattern(regexp = "LEGAL|PHYSIC")
        String customerType,

        @Size(min = 1, max = 50)
        String cardholder,

        @Pattern(regexp = "ACTIVE|INACTIVE|BLOCKED|NEW")
        String cardStatus) {
}
