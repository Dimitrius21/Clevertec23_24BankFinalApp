package ru.clevertec.bank.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.bank.product.domain.entity.Deposit;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
