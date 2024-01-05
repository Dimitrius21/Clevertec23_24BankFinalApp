package ru.clevertec.bank.product.rabbitconsumer;

import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;

@Data
//@Component
public class AccountConsumer {
    private final AccountService accService;

    @RabbitListener(queues = {"#{'${rabbit.queue.account}'}"})
    public void getAccountMessage(String message){

    }



}
