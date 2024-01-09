package ru.clevertec.bank.customer.domain.entity.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.bank.customer.domain.entity.Customer;

import java.time.LocalDate;

public class CustomerListener {

    @PrePersist
    public void prePersist(Customer customer) {
        customer.setRegisterDate(LocalDate.now());
    }

}
