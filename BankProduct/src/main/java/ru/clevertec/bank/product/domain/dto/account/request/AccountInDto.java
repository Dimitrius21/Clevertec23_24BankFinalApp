package ru.clevertec.bank.product.domain.dto.account.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountInDto {
    @Pattern(regexp = "[0-9A-Z]{27,35}")
    private String iban;
    @NotBlank
    private String name;
    private String ibanReadable;
    @PositiveOrZero
    private BigDecimal amount;
    private int currencyCode;
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate openDate;
    @NotNull
    private boolean mainAcc;
    @NotNull
    private UUID customerId;
    @NotNull
    private CustomerType customerType;
    @PositiveOrZero
    private double rate;
}
