package ru.clevertec.bank.product.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankProductType {

    ACCOUNT("/account"),
    CARD("/cards"),
    CREDIT("/credits"),
    DEPOSIT("/deposits"),
    WRONG("wrong");

    private final String name;

    public static BankProductType fromName(String name) {
        for (BankProductType type : BankProductType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return WRONG;
    }

}
