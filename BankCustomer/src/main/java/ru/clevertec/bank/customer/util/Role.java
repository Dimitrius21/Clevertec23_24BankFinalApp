package ru.clevertec.bank.customer.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("ROLE_USER"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    SUPER_USER("ROLE_SUPER_USER");

    private final String name;

}
