package ru.clevertec.bank.product.rabbitconsumer;

import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.service.DepositService;

@Data
//@Component
public class DepositConsumer {
    private final DepositService depositService;

    @RabbitListener(queues = {"#{'${rabbit.queue.deposit}'}"})
    public void getAccountMessage(String message){

    }



}
