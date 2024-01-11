package ru.clevertec.bank.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.bank.product.domain.entity.Card;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, PagingAndSortingRepository<Card, Long> {

    Optional<Card> getCardByCardNumber(String cardNumber);
}
