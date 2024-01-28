package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequest(

        @JsonProperty("customer_type")
        @Pattern(regexp = "LEGAL|PHYSIC", message = "Acceptable customerTypes are only: LEGAL or PHYSIC")
        String customerType,

        @JsonProperty("unp")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String unp,

        @Email
        @JsonProperty("email")
        String email,

        @Digits(integer = 5, fraction = 0)
        @Size(min = 5, max = 5)
        @JsonProperty("phoneCode")
        String phoneCode,

        @Digits(integer = 7, fraction = 0)
        @Size(min = 7, max = 7)
        @JsonProperty("phoneNumber")
        String phoneNumber,

        @NotBlank
        @JsonProperty("customer_fullname")
        String customerFullName) {
}
