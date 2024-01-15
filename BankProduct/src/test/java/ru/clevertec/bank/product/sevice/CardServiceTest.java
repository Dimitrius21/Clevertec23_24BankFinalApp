package ru.clevertec.bank.product.sevice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitPayloadRequest;
import ru.clevertec.bank.product.repository.CardRepository;
import ru.clevertec.bank.product.service.CardService;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    private CardRabbitPayloadRequest cardRabbitPayloadRequest;

    @BeforeEach
    public void init() {
        UUID uuid = UUID.fromString("1a72a000-4b8f-43c5-a889-1ebc6d9dc000");
        cardRabbitPayloadRequest = new CardRabbitPayloadRequest("1000000000000000", "1000 0000 0000 0000", "AAAAAAAAAAAEEEEEEEEEEEEEEEE", uuid, CustomerType.LEGAL, "Mister Holder", CardStatus.NEW);

    }

    @Test
    @DisplayName("when request null return Exception")
    public void saveForRabbitTest_return() {
        cardRabbitPayloadRequest = null;

        RequestBodyIncorrectException exception = Assertions.assertThrows(RequestBodyIncorrectException.class,
                () -> cardService.saveForRabbit(cardRabbitPayloadRequest));
        Assertions.assertEquals("Empty request from Rabbit for save card", exception.getMessage(), "Messages not equals");
    }


}
