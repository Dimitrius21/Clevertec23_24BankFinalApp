package ru.clevertec.bank.product.rabbitconsumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.service.AccountService;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountConsumerTest {

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountConsumer currencyRateConsumeraccountConsumer;

    @Test
    void handleAccountMessage() {
        String mes = "message";
        currencyRateConsumeraccountConsumer.handleAccountMessage(mes);
        verify(service).saveAccountFromRabbit(mes);
    }


}