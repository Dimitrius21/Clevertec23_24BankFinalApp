package ru.clevertec.bank.product.domain.dto.deposit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public record DepositInfoRequest(

        @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
        String customerId,

        @Pattern(regexp = "LEGAL|PHYSIC",
                message = "Acceptable customerTypes are only: LEGAL or PHYSIC")
        String customerType,

        @Valid
        AccInfoRequest accInfo,

        @Valid
        DepInfoRequest depInfo) {
}
