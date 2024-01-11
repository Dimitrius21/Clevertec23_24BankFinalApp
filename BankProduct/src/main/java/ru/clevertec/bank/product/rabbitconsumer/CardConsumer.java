package ru.clevertec.bank.product.rabbitconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitRequest;
import ru.clevertec.bank.product.service.CardService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardConsumer {

    private final CardService cardService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = {"${rabbit.queue.card}"})
    public void onCardMessage(String message) {
        try {
            log.info("Rabbit message: {}", message);
            CardRabbitRequest cardRabbitRequest = objectMapper.readValue(message, CardRabbitRequest.class);
            cardService.saveForRabbit(cardRabbitRequest.payload());
            log.info("Card was saved");
        } catch (JsonProcessingException e) {
            log.error("Occur problem with processing JSON content. {}", e.getMessage());
        }
    }
}
