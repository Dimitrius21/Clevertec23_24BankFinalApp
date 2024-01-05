package ru.clevertec.bank.product.rabbitconsumer;

import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.service.CardService;

@Data
//@Component
public class CardConsumer {
    private final CardService cardService;

    @RabbitListener(queues = {"#{'${rabbit.queue.card}'}"})
    public void getAccountMessage(String message){

    }



}
