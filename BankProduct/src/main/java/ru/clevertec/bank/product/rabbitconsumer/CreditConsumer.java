package ru.clevertec.bank.product.rabbitconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.domain.dto.credit.request.CreditRabbitRequestDTO;
import ru.clevertec.bank.product.service.CreditService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditConsumer {

    private final CreditService creditService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = {"${rabbit.queue.credit}"})
    public void onCreditMessage(String message) {
        try {
            ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();
            CreditRabbitRequestDTO request = objectMapper.readValue(message, CreditRabbitRequestDTO.class);
            log.info("Received request from RabbitMQ:\n{}", prettyPrinter.writeValueAsString(request));
            creditService.saveCreditFromRabbit(message);
            log.info("Request successfully saved in Postgresql");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
