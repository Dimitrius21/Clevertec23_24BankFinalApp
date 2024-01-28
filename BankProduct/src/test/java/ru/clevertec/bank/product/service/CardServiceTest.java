package ru.clevertec.bank.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.bank.product.client.CurrencyRateClient;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponseWithAmount;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.domain.entity.Card;
import ru.clevertec.bank.product.domain.entity.Rate;
import ru.clevertec.bank.product.domain.entity.RateFeign;
import ru.clevertec.bank.product.mapper.CardMapper;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.repository.CardRepository;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.exceptionhandler.exception.GeneralException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CurrencyRateClient currencyRateClient;

    private CardRabbitPayloadRequest cardRabbitPayloadRequest;
    private CardRequest cardRequest;
    private CardUpdateRequest cardUpdateRequest;
    private CardResponse cardResponse;
    private CardResponseWithAmount cardResponseWithAmount;
    private Account account;
    private Card card;
    private UUID uuid;
    private String cardNumber;
    private String iban;
    private RateFeign rateFeign;

    @BeforeEach
    public void init() {
        cardNumber = "1000000000000000";
        uuid = UUID.fromString("1a72a000-4b8f-43c5-a889-1ebc6d9dc000");
        iban = "AAAAAAAAAAAEEEEEEEEEEEEEEEE";
        String cardholder = "Mister Holder";
        LocalDateTime localDateTime = LocalDateTime.of(2023, 12, 22, 13, 55);
        cardRabbitPayloadRequest = new CardRabbitPayloadRequest(cardNumber, "1000 0000 0000 0000",
                iban, uuid, CustomerType.LEGAL, cardholder, CardStatus.NEW);
        card = new Card(cardNumber, uuid, CustomerType.LEGAL, cardholder, CardStatus.NEW, account);
        account = new Account(iban, "Account Name", 2000L, "BYN", LocalDate.now(),
                true, uuid, CustomerType.LEGAL, 0.01D, List.of(card));
        cardRequest = new CardRequest(cardNumber, " ", iban, uuid.toString(), "LEGAL", cardholder, "NEW");
        cardResponse = new CardResponse(cardNumber, null, uuid, CustomerType.LEGAL, cardholder, CardStatus.NEW);
        rateFeign = new RateFeign(1L, localDateTime, List.of(new Rate(1L, 3.33d, 3.43d, "EUR", "BYN"),
                new Rate(1L, 3.05d, 3.15d, "USD", "BYN")));
        cardResponseWithAmount = new CardResponseWithAmount(cardNumber, iban, uuid, CustomerType.LEGAL, cardholder, CardStatus.NEW, null);
        cardUpdateRequest = new CardUpdateRequest(iban, "LEGAL", "NEW");
    }

    @Test
    @DisplayName("saveForRabbit return void when request correct and card not present")
    void saveForRabbitTest_cardNotPresent() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());
        when(cardMapper.toCard(cardRabbitPayloadRequest)).thenReturn(card);
        when(accountRepository.findById(iban)).thenReturn(Optional.of(account));

        cardService.saveForRabbit(cardRabbitPayloadRequest);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("saveForRabbit return void when request correct and card present")
    void saveForRabbitTest_cardPresent() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));
        doNothing().when(cardMapper).updateFromRabbitDto(cardRabbitPayloadRequest, card);
        when(accountRepository.findById(iban)).thenReturn(Optional.of(account));

        cardService.saveForRabbit(cardRabbitPayloadRequest);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("saveForRabbit should return Exception when request null")
    void saveForRabbitTest_returnException() {
        cardRabbitPayloadRequest = null;

        RequestBodyIncorrectException exception = assertThrows(RequestBodyIncorrectException.class,
                () -> cardService.saveForRabbit(cardRabbitPayloadRequest));
        assertThat(exception.getMessage()).isEqualTo("Empty request from Rabbit for save card");
    }

    @Test
    @DisplayName("saveForRabbit should return Exception when request with absent iban")
    void saveForRabbitTest_whenRequestWithAbsentIban_returnException() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());
        when(cardMapper.toCard(cardRabbitPayloadRequest)).thenReturn(card);

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.saveForRabbit(cardRabbitPayloadRequest));
        assertThat(exception.getMessage()).isEqualTo(String.format("Account with iban=%s not found", iban));
    }

    @Test
    @DisplayName("save should return CardResponse when request correct and card not present")
    void saveTest_cardNotPresent() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());
        when(cardMapper.toCard(cardRequest)).thenReturn(card);
        when(accountRepository.findById(iban)).thenReturn(Optional.of(account));
        card.setAccount(account);
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toCardResponse(card)).thenReturn(cardResponse);

        CardResponse actual = cardService.save(cardRequest);
        assertThat(actual).isEqualTo(cardResponse);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("save should return Exception when request null")
    void saveTest_returnException() {
        cardRequest = null;

        RequestBodyIncorrectException exception = assertThrows(RequestBodyIncorrectException.class,
                () -> cardService.save(cardRequest));
        assertThat(exception.getMessage()).isEqualTo("Empty request for save card");
    }

    @Test
    @DisplayName("save should return Exception when cardNumber is present")
    void saveTest_whenCardNumberPresent_returnException() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));

        GeneralException exception = assertThrows(GeneralException.class,
                () -> cardService.save(cardRequest));
        assertThat(exception.getMessage()).isEqualTo(String.format("Card with number: %s is already exist", cardNumber));
    }

    @Test
    @DisplayName("save should return Exception when request with absent in db iban ")
    void saveTest_whenRequestWithAbsentIban_returnException() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());
        when(cardMapper.toCard(cardRequest)).thenReturn(card);
        when(accountRepository.findById(iban)).thenReturn(Optional.empty());

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.save(cardRequest));
        assertThat(exception.getMessage()).isEqualTo(String.format("Account with iban=%s not found", iban));
    }

    @Test
    @DisplayName("findById should return CardResponseWithAmount when request correct")
    void findByIdTest() {
        card.setAccount(account);
        when(cardRepository.findWithAccountByCardNumber(cardNumber)).thenReturn(Optional.of(card));
        when(currencyRateClient.getCurrent()).thenReturn(rateFeign);
        when(cardMapper.toCardResponseWithAmounts(card)).thenReturn(cardResponseWithAmount);

        CardResponseWithAmount actual = cardService.findById(cardNumber);
        assertThat(actual).isEqualTo(cardResponseWithAmount);
    }

    @Test
    @DisplayName("findById should return CardResponseWithAmount when request correct and card not present")
    void findByIdTest_when() {
        when(cardRepository.findWithAccountByCardNumber(cardNumber)).thenReturn(Optional.empty());

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.findById(cardNumber));
        assertThat(exception.getMessage()).isEqualTo(String.format("Card with id=%s not found", cardNumber));
    }

    @Test
    @DisplayName("findAll should return Page<CardResponse>")
    void findAllTest() {
        Pageable pageable = PageRequest.of(0, 5);
        when(cardRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(card)));
        when(cardMapper.toCardResponse(card)).thenReturn(cardResponse);

        Page<CardResponse> actual = cardService.findAll(pageable);
        assertThat(actual).contains(cardResponse);
    }

    @Test
    @DisplayName("findAll should return empty Page")
    void findAllTest_returnEmpty() {
        Pageable pageable = PageRequest.of(5, 5);
        when(cardRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<CardResponse> actual = cardService.findAll(pageable);
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("findByCustomerId should return CardResponse when uuid present")
    void findByCustomerIdTest() {
        when(cardRepository.findByCustomerId(uuid)).thenReturn(Optional.of(card));
        when(cardMapper.toCardResponse(card)).thenReturn(cardResponse);

        CardResponse actual = cardService.findByCustomerId(uuid);
        assertThat(actual).isEqualTo(cardResponse);
    }

    @Test
    @DisplayName("findByCustomerId should return ResourceNotFountException when uuid not present")
    void findByCustomerIdTest_returnException() {
        when(cardRepository.findByCustomerId(uuid)).thenReturn(Optional.empty());

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.findByCustomerId(uuid));
        assertThat(exception.getMessage()).isEqualTo(String.format("Card with customer uuid=%s not found", uuid));
    }

    @Test
    @DisplayName("update should return CardResponse when request correct and card not present")
    void updateTest() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));
        when(accountRepository.findById(iban)).thenReturn(Optional.of(account));
        doNothing().when(cardMapper).updateFromDto(cardUpdateRequest, card);
        when(cardRepository.save(card)).thenReturn(card);
        card.setAccount(account);
        when(cardMapper.toCardResponse(card)).thenReturn(cardResponse);


        CardResponse actual = cardService.update(cardNumber, cardUpdateRequest);
        assertThat(actual).isEqualTo(cardResponse);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("update should return RequestBodyIncorrectException when request null")
    void updateTest_whenRequestNull_returnException() {
        cardUpdateRequest = null;

        RequestBodyIncorrectException exception = assertThrows(RequestBodyIncorrectException.class,
                () -> cardService.update(cardNumber, cardUpdateRequest));
        assertThat(exception.getMessage()).isEqualTo("Empty request for update card");
    }

    @Test
    @DisplayName("update should return RequestBodyIncorrectException when cardNumber not present")
    void updateTest_returnException_whenCardAbsent() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.update(cardNumber, cardUpdateRequest));
        assertThat(exception.getMessage()).isEqualTo(String.format("Card with id=%s not found", cardNumber));
    }

    @Test
    @DisplayName("update should return RequestBodyIncorrectException when cardNumber not present")
    void updateTest_returnException_whenAccountAbsent() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));
        when(accountRepository.findById(iban)).thenReturn(Optional.empty());

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.update(cardNumber, cardUpdateRequest));
        assertThat(exception.getMessage()).isEqualTo(String.format("Account with iban=%s not found", iban));
    }

    @Test
    @DisplayName("remove should return cardNumber when cardNumber present")
    void removeTest() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));
        doNothing().when(cardRepository).deleteById(cardNumber);

        String actual = cardService.remove(cardNumber);
        assertThat(actual).isEqualTo(cardNumber);
    }

    @Test
    @DisplayName("remove should return ResourceNotFountException when cardNumber not present")
    void removeTest_returnException() {
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());

        ResourceNotFountException exception = assertThrows(ResourceNotFountException.class,
                () -> cardService.remove(cardNumber));
        assertThat(exception.getMessage()).isEqualTo(String.format("Card with id=%s not found", cardNumber));
    }
}
