package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerRabbitPayloadRequest(

        @JsonProperty("customer_id")
        UUID customerId,

        @JsonProperty("customer_type")
        String customerType,

        @JsonProperty("unp")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String unp,

        @JsonProperty("register_date")
        @JsonFormat(pattern = "dd.MM.yyyy")
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
