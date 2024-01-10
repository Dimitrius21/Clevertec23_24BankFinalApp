package ru.clevertec.bank.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.bank.customer.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByCustomerIdAndDeletedFalse(UUID id);

    Optional<Customer> findByCustomerIdAndDeletedTrue(UUID id);

    Page<Customer> findAllByDeletedFalse(Pageable pageable);

}
