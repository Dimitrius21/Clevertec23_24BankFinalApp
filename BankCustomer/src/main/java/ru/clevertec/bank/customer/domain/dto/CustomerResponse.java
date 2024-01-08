package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.clevertec.bank.customer.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponse(UUID id,
                               CustomerType type,

                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               String unp,

                               LocalDate registerDate,
                               String email,
                               String phoneCode,
                               String phoneNumber,
                               String fullName) {
}
