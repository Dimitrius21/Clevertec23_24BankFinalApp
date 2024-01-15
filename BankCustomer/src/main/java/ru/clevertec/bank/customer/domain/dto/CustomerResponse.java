package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.clevertec.bank.customer.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponse(
        @JsonProperty("customer_id")
        UUID customerId,

        @JsonProperty("customer_type")
        CustomerType customerType,

        @JsonProperty("unp")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String unp,

        @JsonProperty("register_date")
        LocalDate registerDate,

        @JsonProperty("email")
        String email,

        @JsonProperty("phoneCode")
        String phoneCode,

        @JsonProperty("phoneNumber")
        String phoneNumber,

        @JsonProperty("customer_fullname")
        String customerFullName) {
}
