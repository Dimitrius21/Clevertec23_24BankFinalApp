package ru.clevertec.bank.product.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.bank.product.domain.entity.Card;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, String>, PagingAndSortingRepository<Card, String> {

    Optional<Card> findCardByCardNumber(String cardNumber);

    Optional<Card> findByCustomerId(UUID uuid);

    @EntityGraph(attributePaths = {"account"})
    Optional<Card> findWithAccountByCardNumber(String cardNumber);

}
