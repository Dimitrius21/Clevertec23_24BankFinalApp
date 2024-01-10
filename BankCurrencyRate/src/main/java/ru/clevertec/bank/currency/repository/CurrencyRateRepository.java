package ru.clevertec.bank.currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.bank.currency.domain.entity.Rates;

import java.time.LocalDateTime;

@Repository
public interface CurrencyRateRepository extends JpaRepository<Rates, Long> {
    Rates findFirstByStartBeforeOrderByStartDesc(LocalDateTime start);

}
