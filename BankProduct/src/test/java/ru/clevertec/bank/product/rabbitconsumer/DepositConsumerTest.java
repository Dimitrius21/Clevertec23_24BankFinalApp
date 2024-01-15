package ru.clevertec.bank.product.rabbitconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.dto.HeaderRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitPayloadRequest;
import ru.clevertec.bank.product.service.DepositService;
import ru.clevertec.bank.product.testutil.deposit.DepositRabbitPayloadRequestTestBuilder;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepositConsumerTest {

    @InjectMocks
    private DepositConsumer depositConsumer;
    @Mock
    private DepositService depositService;
    @Spy
    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<DepositRabbitPayloadRequest> captor;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("test onDepositMessage should capture value")
    void testOnDepositMessageShouldCaptureValue() throws JsonProcessingException {
        DepositRabbitInfoRequest request = new DepositRabbitInfoRequest(new HeaderRequest("customer"),
                DepositRabbitPayloadRequestTestBuilder.aDepositRabbitPayloadRequest().build());
        String message = objectMapper.writeValueAsString(request);

        doNothing()
                .when(depositService)
                .save(request.payload());

        depositConsumer.onDepositMessage(message);
        verify(depositService).save(captor.capture());

        DepositRabbitPayloadRequest captorValue = captor.getValue();
        assertThat(captorValue).isEqualTo(request.payload());
    }

    @Test
    @DisplayName("test onDepositMessage should throw ResourceNotFountException with expected message")
    void testShouldThrowResourceNotFountException() {
        String iban = "Not  valid json";
        String expectedMessage = "Unrecognized token 'Not': was expecting (JSON String, Number, Array," +
                                 " Object or token 'null', 'true' or 'false')\n at [Source: (String)\"Not  valid json\";" +
                                 " line: 1, column: 4]";

        Exception exception = assertThrows(ResourceNotFountException.class, () -> depositConsumer.onDepositMessage(iban));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
