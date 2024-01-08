package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record CustomerRequest(String type,

                              @JsonInclude(JsonInclude.Include.NON_NULL)
                              String unp,

                              String email,
                              String phoneCode,
                              String phoneNumber,
                              String fullName) {
}
