package ru.clevertec.bank.product.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.domain.entity.Card;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class CardRepositoryTest extends BaseIntegrationTest {

    private final CardRepository cardRepository;

    @Test
    void saveTest() {
        String cardNumber = "5200000000001099";
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEE0";
        String cardholder = "Jack Nikson";
        Card card = getCard();
        Card actual = cardRepository.save(card);
        assertThat(actual.getCardNumber()).isEqualTo(cardNumber);
        assertThat(actual.getAccount().getIban()).isEqualTo(iban);
        assertThat(actual.getCardholder()).isEqualTo(cardholder);
        assertThat(actual.getCardStatus()).isEqualTo(CardStatus.INACTIVE);
        assertThat(actual.getCustomerType()).isEqualTo(CustomerType.LEGAL);
    }

    @Test
    void findByIdTest() {
        String cardNumber = "5200000000001099";
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEE0";
        Optional<Card> card = cardRepository.findById(cardNumber);
        assertThat(card.get().getCardNumber()).isEqualTo(cardNumber);
        assertThat(card.get().getAccount().getIban()).isEqualTo(iban);
    }

    @Test
    void findAllTest() {
        String cardNumber = "5200000000001099";
        Sort sort = Sort.by(Sort.Order.desc("cardNumber"));
        Page<Card> actual = cardRepository.findAll(PageRequest.of(0, 1, sort));
        assertThat(actual).hasSize(1)
                .flatExtracting(Card::getCardNumber)
                .contains(cardNumber);
    }

    @Test
    void findByCustomerIdTest() {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        Optional<Card> actual = cardRepository.findByCustomerId(uuid);
        assertThat(actual.get().getCustomerId()).isEqualTo(uuid);
    }

    @Test
    void deleteByIdTest() {
        String cardNumber = "5200000000001099";
        cardRepository.deleteById(cardNumber);
        Optional<Card> card = cardRepository.findById(cardNumber);
        assertThat(card.isEmpty()).isTrue();
    }

    @Test
    void findWithAccountByCardNumber() {
        String cardNumber = "5200000000001099";
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEE0";
        Optional<Card> actual = cardRepository.findWithAccountByCardNumber(cardNumber);
        assertThat(actual.get().getCardNumber()).isEqualTo(cardNumber);
        assertThat(actual.get().getAccount().getIban()).isEqualTo(iban);
    }

    private Card getCard() {
        String cardNumber = "5200000000001099";
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEE0";
        return new Card(cardNumber, uuid, CustomerType.LEGAL, "Jack Nikson", CardStatus.INACTIVE, new Account(
                iban, "Main", 20000, "BYN", LocalDate.of(2024, 1, 7),
                true, uuid, CustomerType.LEGAL, 0.0, null));
    }

}
