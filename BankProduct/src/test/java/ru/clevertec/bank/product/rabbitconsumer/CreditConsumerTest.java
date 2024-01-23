package ru.clevertec.bank.product.rabbitconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.service.CreditService;
import ru.clevertec.bank.product.util.CustomerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditConsumerTest {

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CreditConsumer creditConsumer;

    @Spy
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void onCreditMessage() {
        CreditResponseDTO response = getCreditResponseDTO();

        String message = """
                {
                    "header": {
                        "message_type": "credit-details"
                    },
                    "payload": {
                        "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                        "contractNumber": "56-5656556-9-9",
                        "contractStartDate": "01.01.2023",
                        "totalDebt": 100,
                        "currentDebt": 100,
                        "currency": "BYN",
                        "repaymentDate": "10.12.2024",
                        "rate": 22.8,
                        "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                        "possibleRepayment": true,
                        "isClosed": false,
                        "customer_type" : "LEGAL"
                    }
                }""";

        when(creditService.saveCreditFromRabbit(message)).thenReturn(response);
        creditConsumer.onCreditMessage(message);

        verify(creditService).saveCreditFromRabbit(message);
    }

    private CreditResponseDTO getCreditResponseDTO() {
        return new CreditResponseDTO("11-0216444-2-0",
                UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"), LocalDate.of(2023, 1, 1),
                BigDecimal.valueOf(8113.99), BigDecimal.valueOf(361.99), "BYN",
                LocalDate.of(2024, 1, 1), 22.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }
}
