package ru.clevertec.bank.product.domain.dto.card.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardUpdateRequest(
                                @Pattern(regexp = "^[A-Z]{27}$")
                                String iban,

                                @Pattern(regexp = "LEGAL|PHYSIC")
                                String customerType,

                                @Pattern(regexp = "ACTIVE|INACTIVE|BLOCKED|NEW")
                                String cardStatus) {
}
