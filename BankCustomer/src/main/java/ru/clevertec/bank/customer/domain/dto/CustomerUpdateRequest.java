package ru.clevertec.bank.customer.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(

        @Email
        String email,

        @Digits(integer = 5, fraction = 0)
        @Size(min = 5, max = 5)
        String phoneCode,

        @Digits(integer = 7, fraction = 0)
        @Size(min = 7, max = 7)
        String phoneNumber) {
}
