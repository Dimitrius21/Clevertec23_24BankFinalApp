package ru.clevertec.bank.product.domain.dto.card.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CardUpdateRequest(
                                @Pattern(regexp = "^[A-Z]{27}$")
                                String iban,

                                @Pattern(regexp = "LEGAL|PHYSIC")
                                CustomerType customerType,

                                @Pattern(regexp = "ACTIVE|INACTIVE|BLOCKED|NEW")
                                CardStatus cardStatus) {
}
