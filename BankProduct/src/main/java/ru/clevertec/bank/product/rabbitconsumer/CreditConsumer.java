package ru.clevertec.bank.product.rabbitconsumer;

import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.service.CreditService;

@Data
//@Component
public class CreditConsumer {
    private final CreditService creditService;

    @RabbitListener(queues = {"#{'${rabbit.queue.credit}'}"})
    public void getAccountMessage(String message){

    }



}
