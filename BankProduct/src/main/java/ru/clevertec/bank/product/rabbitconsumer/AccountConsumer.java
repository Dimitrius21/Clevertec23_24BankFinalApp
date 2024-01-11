package ru.clevertec.bank.product.rabbitconsumer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

@Slf4j
@Data
@Component
public class AccountConsumer {
    private final AccountService accService;

    @RabbitListener(queues = {"#{'${rabbit.queue.account}'}"})
    public void getAccountMessage(String message) {
        try {
            accService.saveAccountFromRabbit(message);
        }
        catch (RequestBodyIncorrectException e){
            log.error(e.getMessage());
        }
    }
}
