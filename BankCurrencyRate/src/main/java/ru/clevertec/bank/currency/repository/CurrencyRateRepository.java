package ru.clevertec.bank.currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.bank.currency.domain.entity.Rates;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CurrencyRateRepository extends JpaRepository<Rates, Long> {
    List<Rates> findByStart(LocalDateTime start);

/*    //SELECT * FROM TableName WHERE id=(SELECT max(id) FROM TableName);
    @Query(value = "SELECT * FROM rates_list rs JOIN rates r ON rs.id = r.rates_list_id WHERE rs.id = (SELECT max(id) FROM rates_list)",
            nativeQuery = true)
    Rates findLastRatesN();*/

    @Query("select rs from Rates rs join rs.exchangeRates r where rs.id = (select max(rts.id) from Rates rts)")
    Rates findLastRates();
}
