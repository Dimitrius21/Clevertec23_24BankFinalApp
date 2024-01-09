package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerRequest(
        @JsonProperty("customer_type")
        String customerType,

        @JsonProperty("unp")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String unp,

        @JsonProperty("email")
        String email,

        @JsonProperty("phoneCode")
        String phoneCode,

        @JsonProperty("phoneNumber")
        String phoneNumber,

        @JsonProperty("customer_fullname")
        String customerFullName) {
}
