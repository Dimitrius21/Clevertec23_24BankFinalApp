package ru.clevertec.bank.customer.domain.dto;

import jakarta.validation.constraints.Pattern;

public record JwtRequest(String id,

                         @Pattern(regexp = "USER|ADMINISTRATOR|SUPER_USER",
                                 message = "Acceptable roles are only: USER, ADMINISTRATOR or SUPER_USER")
                         String role) {
}
