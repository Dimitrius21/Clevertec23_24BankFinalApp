package ru.clevertec.bank.product.rabbitconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitInfoRequest;
import ru.clevertec.bank.product.service.DepositService;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositConsumer {

    private final DepositService depositService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbit.queue.deposit}")
    public void onDepositMessage(String message) {
        try {
            ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();
            DepositRabbitInfoRequest request = objectMapper.readValue(message, DepositRabbitInfoRequest.class);
            log.info("Received request from RabbitMQ:\n{}", prettyPrinter.writeValueAsString(request));
            depositService.save(request.payload());
            log.info("Request successfully saved in Postgresql");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new ResourceNotFountException(e.getMessage()); // TODO add better exception later
        }
    }

}
