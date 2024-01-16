package ru.clevertec.bank.currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.bank.currency.domain.entity.Rates;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Repository
public interface CurrencyRateRepository extends JpaRepository<Rates, Long> {
    Rates findFirstByStartBeforeOrderByStartDesc(ZonedDateTime start);

}
