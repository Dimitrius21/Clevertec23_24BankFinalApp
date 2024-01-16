package ru.clevertec.bank.currency.rabbitconsumer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.currency.service.CurrencyRateService;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class CurrencyRateConsumer {
    private final CurrencyRateService service;

    @RabbitListener(queues = {"${rabbit.queue.rates}"})
    public void handleAccountMessage(String message) {
        try {
            service.saveRatesFromRabbit(message);
        } catch (RequestBodyIncorrectException e) {
            log.error(e.getMessage());
        }
    }
}
