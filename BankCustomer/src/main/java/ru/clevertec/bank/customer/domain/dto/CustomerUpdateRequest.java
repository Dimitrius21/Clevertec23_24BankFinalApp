package ru.clevertec.bank.customer.domain.dto;

public record CustomerUpdateRequest(String email,

                                    String phoneCode,

                                    String phoneNumber) {
}
