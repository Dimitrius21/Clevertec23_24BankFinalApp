package ru.clevertec.bank.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.bank.customer.domain.entity.Customer;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
