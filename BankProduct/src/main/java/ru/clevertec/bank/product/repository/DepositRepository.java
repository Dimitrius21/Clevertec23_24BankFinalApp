package ru.clevertec.bank.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.bank.product.domain.entity.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
