package ru.clevertec.bank.customer.consumer;

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
import ru.clevertec.bank.customer.domain.dto.CustomerRabbitPayloadRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerRabbitRequest;
import ru.clevertec.bank.customer.domain.dto.HeaderRequest;
import ru.clevertec.bank.customer.service.CustomerService;
import ru.clevertec.bank.customer.testutil.CustomerRabbitPayloadRequestTestBuilder;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerConsumerTest {

    @InjectMocks
    private CustomerConsumer customerConsumer;
    @Mock
    private CustomerService customerService;
    @Spy
    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<CustomerRabbitPayloadRequest> captor;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("test onCustomerMessage should capture value")
    void testOnCustomerMessageShouldCaptureValue() throws JsonProcessingException {
        CustomerRabbitRequest request = new CustomerRabbitRequest(new HeaderRequest("customer"),
                CustomerRabbitPayloadRequestTestBuilder.aCustomerRabbitPayloadRequest().build());
        String message = objectMapper.writeValueAsString(request);

        doNothing()
                .when(customerService)
                .save(request.payload());

        customerConsumer.onCustomerMessage(message);
        verify(customerService).save(captor.capture());

        CustomerRabbitPayloadRequest captorValue = captor.getValue();
        assertThat(captorValue).isEqualTo(request.payload());
    }

    @Test
    @DisplayName("test onDepositMessage should throw InternalServerErrorException with expected message")
    void testShouldThrowInternalServerErrorException() {
        String message = "Not  valid json";
        String expectedMessage = "Unrecognized token 'Not': was expecting (JSON String, Number, Array," +
                                 " Object or token 'null', 'true' or 'false')\n at [Source: (String)\"Not  valid json\";" +
                                 " line: 1, column: 4]";

        Exception exception = assertThrows(InternalServerErrorException.class, () -> customerConsumer.onCustomerMessage(message));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
