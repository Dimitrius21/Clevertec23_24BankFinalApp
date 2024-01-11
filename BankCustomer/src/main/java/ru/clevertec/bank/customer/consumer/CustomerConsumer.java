package ru.clevertec.bank.customer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.customer.domain.dto.CustomerRabbitRequest;
import ru.clevertec.bank.customer.service.CustomerService;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerConsumer {

    private final CustomerService customerService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbit.queue.customer}")
    public void onCustomerMessage(String message) {
        try {
            ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();
            CustomerRabbitRequest request = objectMapper.readValue(message, CustomerRabbitRequest.class);
            log.info("Received request from RabbitMQ:\n{}", prettyPrinter.writeValueAsString(request));
            customerService.save(request.payload());
            log.info("Request successfully saved in Postgresql");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
