package ru.clevertec.bank.customer.domain.entity.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.bank.customer.domain.entity.Customer;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class CustomerListener {

    @PrePersist
    public void prePersist(Customer customer) {
        if (Objects.isNull(customer.getCustomerId())) {
            customer.setCustomerId(UUID.randomUUID());
        }
        if (Objects.isNull(customer.getRegisterDate())) {
            customer.setRegisterDate(LocalDate.now());
        }
    }

}
