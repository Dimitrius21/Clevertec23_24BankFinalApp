package ru.clevertec.bank.customer.domain.dto;

public record CustomerRabbitRequest(HeaderRequest header,
                                    CustomerRabbitPayloadRequest payload) {
}
