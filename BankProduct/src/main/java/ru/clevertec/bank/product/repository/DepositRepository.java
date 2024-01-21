package ru.clevertec.bank.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.clevertec.bank.product.domain.entity.Deposit;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, String>, JpaSpecificationExecutor<Deposit> {

    boolean existsByCustomerId(UUID id);

}
