package ru.clevertec.bank.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.bank.product.domain.entity.Credit;

import java.util.List;
import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, String> {

    List<Credit> findAllByCustomerId(UUID customerId);

    boolean existsByCustomerId(UUID id);

}
