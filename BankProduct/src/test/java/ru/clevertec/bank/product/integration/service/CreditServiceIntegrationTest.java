package ru.clevertec.bank.product.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.service.CreditService;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class CreditServiceIntegrationTest extends BaseIntegrationTest {

    private final CreditService creditService;

    @Test
    void findByContractNumberShouldThrowNotFoundException() {
        String contractNumber = "11-9999994-2-1";
        String expectedMessage = "Credit with contractNumber = %s not found".formatted(contractNumber);

        Exception exception = assertThrows(
                ResourceNotFountException.class, () -> creditService.findByContractNumber(contractNumber));

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    void findByContractNumberTest() {
        String contractNumber = "11-0216444-2-0";
        CreditResponseDTO expected = getCreditResponseDTO();

        CreditResponseDTO result = creditService.findByContractNumber(contractNumber);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findAllTest() {
        CreditResponseDTO response = getCreditResponseDTO();
        PageRequest pageable = PageRequest.of(0, 6);

        Page<CreditResponseDTO> actual = creditService.findAll(pageable);

        assertThat(actual).hasSize(6).contains(response);
    }

    @Test
    void findAllByClientIdTest() {
        CreditResponseDTO response = getCreditResponseDTO();
        UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");

        List<CreditResponseDTO> actual = creditService.findAllByClientId(customerId);

        assertThat(actual).contains(response);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionTest() {
        String contractNumber = "11-9999999-2-1567";
        String expectedMessage = "Credit with contractNumber = %s not found".formatted(contractNumber);

        Exception exception = assertThrows(
                ResourceNotFountException.class, () -> creditService.deleteByContractNumber(contractNumber));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void deleteByContractNumberTest() {
        String contractNumber = "11-0216444-2-0";
        DeleteResponse response = new DeleteResponse(
                "Credit with contractNumber %s was successfully deleted".formatted(contractNumber));

        DeleteResponse actual = creditService.deleteByContractNumber(contractNumber);

        assertThat(actual).isEqualTo(response);
    }

    @Test
    void saveTest() {
        String contractNumber = "11-9999999-2-1";
        CreateCreditDTO request = getCreateCreditDTO(contractNumber);

        CreditResponseDTO actual = creditService.save(request);

        assertThat(actual.getContractNumber()).isEqualTo(contractNumber);
    }

    @Test
    void saveShouldThrowRequestBodyIncorrectExceptionTest() {
        String contractNumber = "11-0216444-2-0";
        CreateCreditDTO request = getCreateCreditDTO(contractNumber);
        String expectedMessage = "Credit with such contractNumber already exist";

        Exception exception = assertThrows(RequestBodyIncorrectException.class, () -> creditService.save(request));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void saveCreditFromRabbitTest() {
        String contractNumber = "56-5656556-9-9";
        String message = """
                {
                    "header": {
                        "message_type": "credit-details"
                    },
                    "payload": {
                        "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                        "contractNumber": "56-5656556-9-9",
                        "contractStartDate": "01.01.2024",
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

        CreditResponseDTO response = creditService.saveCreditFromRabbit(message);

        assertThat(response.getContractNumber()).isEqualTo(contractNumber);
    }

    @Test
    void updateByContractNumberTest() {
        String contractNumber = "18-0265444-2-1";
        UpdateCreditDTO request = getUpdateCreditDTO();

        CreditResponseDTO actual = creditService.updateByContractNumber(contractNumber, request);

        assertThat(actual.getRate()).isEqualTo(request.getRate());
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionTest() {
        String contractNumber = "11-9999999-2-1567";
        UpdateCreditDTO request = getUpdateCreditDTO();
        String expectedMessage = "Credit with contractNumber = %s not found".formatted(contractNumber);

        Exception exception = assertThrows(
                ResourceNotFountException.class, () -> creditService.updateByContractNumber(contractNumber, request));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }


    private CreateCreditDTO getCreateCreditDTO(String contractNumber) {
        return new CreateCreditDTO(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                LocalDate.of(2024, 1, 1), BigDecimal.valueOf(100),
                BigDecimal.valueOf(100), "BYN",
                LocalDate.of(2024, 12, 10), 22.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, true, CustomerType.LEGAL);
    }

    private CreditResponseDTO getCreditResponseDTO() {
        return new CreditResponseDTO("11-0216444-2-0",
                UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"), LocalDate.of(2023, 1, 1),
                BigDecimal.valueOf(8113.99), BigDecimal.valueOf(361.99), "BYN",
                LocalDate.of(2024, 1, 1), 22.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private UpdateCreditDTO getUpdateCreditDTO() {
        return new UpdateCreditDTO(LocalDate.of(2025, 12, 10), 30.0, true,
                false);
    }
}
