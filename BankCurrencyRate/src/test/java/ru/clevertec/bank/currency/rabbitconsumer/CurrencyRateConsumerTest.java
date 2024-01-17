package ru.clevertec.bank.currency.rabbitconsumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.currency.service.CurrencyRateService;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyRateConsumerTest {

    @Mock
    private CurrencyRateService service;

    @InjectMocks
    private CurrencyRateConsumer currencyRateConsumer;

    @Test
    void getAccountMessageTest() {
        String mes = "message";
        currencyRateConsumer.handleAccountMessage(mes);
        verify(service).saveRatesFromRabbit(mes);
    }
}