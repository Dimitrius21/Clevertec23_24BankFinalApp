package ru.clevertec.bank.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.bank.product.domain.entity.Deposit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @EntityGraph(attributePaths = {"account"})
    Optional<Deposit> findWithAccountById(Long id);

    @Query("""
            SELECT d FROM Deposit d
            JOIN FETCH d.account
            """)
    Page<Deposit> findAllWithAccounts(Pageable pageable);

    List<Deposit> findAllByExpDateAndAutoRenew(LocalDate expDate, Boolean autoRenew);

}
