package ru.clevertec.bank.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.repository.CreditRepository;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditService creditService;

    @Mock
    private CreditMapper creditMapper;

    @Spy
    private static ObjectMapper jacksonMapper = new ObjectMapper();

    @BeforeAll
    public static void setJackson() {
        jacksonMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void findByContractNumberTest() {
        String contractNumber = "11-0216444-2-0";
        Credit credit = getCredit(contractNumber);
        CreditResponseDTO expected = getCreditResponseDTO(credit);

        when(creditRepository.findById(contractNumber)).thenReturn(Optional.of(credit));
        when(creditMapper.toCreditResponseDTO(credit)).thenReturn(getCreditResponseDTO(credit));

        CreditResponseDTO result = creditService.findByContractNumber(contractNumber);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void findByContractNumberShouldThrowNotFoundException() {
        String contractNumber = "11-0216444-2-1";
        String expectedMessage = "Credit with contractNumber = %s not found".formatted(contractNumber);

        when(creditRepository.findById(contractNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                ResourceNotFountException.class, () -> creditService.findByContractNumber(contractNumber));

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    void findAllTest() {
        Credit credit = getCredit("11-0216444-2-0");
        CreditResponseDTO response = getCreditResponseDTO(credit);
        Page<Credit> credits = new PageImpl<>(List.of(credit));
        PageRequest pageable = PageRequest.of(0, 1);

        when(creditRepository.findAll(pageable)).thenReturn(credits);
        when(creditMapper.toCreditResponseDTO(credit)).thenReturn(response);

        Page<CreditResponseDTO> actual = creditService.findAll(pageable);

        Assertions.assertThat(actual).hasSize(1).contains(response);
    }

    @Test
    void findAllByClientIdTest() {
        Credit credit = getCredit("11-0216444-2-0");
        CreditResponseDTO response = getCreditResponseDTO(credit);
        List<Credit> credits = List.of(credit);
        UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");

        when(creditRepository.findAllByCustomerId(customerId)).thenReturn(credits);
        when(creditMapper.toCreditResponseDTO(credit)).thenReturn(response);

        List<CreditResponseDTO> actual = creditService.findAllByClientId(customerId);

        Assertions.assertThat(actual).contains(response);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionTest() {
        String contractNumber = "11-0216444-2-1";
        String expectedMessage = "Credit with contractNumber = %s not found".formatted(contractNumber);

        Exception exception = assertThrows(
                ResourceNotFountException.class, () -> creditService.deleteByContractNumber(contractNumber));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void deleteByContractNumberTest() {
        String contractNumber = "11-0216444-2-1";
        Credit credit = getCredit(contractNumber);
        DeleteResponse response = new DeleteResponse("Credit with contract number %s was successfully deleted"
                .formatted(contractNumber));

        when(creditRepository.findById(contractNumber)).thenReturn(Optional.of(credit));
        doNothing().when(creditRepository).delete(credit);

        DeleteResponse actual = creditService.deleteByContractNumber(contractNumber);

        assertThat(actual).isEqualTo(response);
    }


    @Test
    void saveTest() {
        String contractNumber = "11-0216444-2-1";
        Credit credit = getCredit(contractNumber);
        CreateCreditDTO request = getCreateCreditDTO(contractNumber);
        CreditResponseDTO response = getCreditResponseDTO(credit);

        when(creditMapper.toCreditResponseDTO(credit)).thenReturn(response);
        when(creditMapper.toCredit(request)).thenReturn(credit);
        when(creditRepository.findById(contractNumber)).thenReturn(Optional.empty());
        when(creditRepository.save(credit)).thenReturn(credit);

        CreditResponseDTO actual = creditService.save(request);

        Assertions.assertThat(actual).isEqualTo(response);
    }

    @Test
    void saveShouldThrowRequestBodyIncorrectExceptionTest() {
        String contractNumber = "11-0216444-2-1";
        Credit credit = getCredit(contractNumber);
        CreateCreditDTO request = getCreateCreditDTO(contractNumber);
        String expectedMessage = "Credit with such contractNumber already exist";

        when(creditMapper.toCredit(request)).thenReturn(credit);
        when(creditRepository.findById(contractNumber)).thenReturn(Optional.of(credit));

        Exception exception = assertThrows(RequestBodyIncorrectException.class, () -> creditService.save(request));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void saveCreditFromRabbitTest() {
        String message = """
                {
                    "header": {
                        "message_type": "credit-details"
                    },
                    "payload": {
                        "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                        "contractNumber": "11-0216444-2-1",
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
        String contractNumber = "11-0216444-2-1";
        Credit credit = getCredit(contractNumber);
        CreditResponseDTO response = getCreditResponseDTO(credit);
        CreateCreditDTO request = getCreateCreditDTO(contractNumber);

        when(creditMapper.toCredit(request)).thenReturn(credit);
        when(creditMapper.toCreditResponseDTO(credit)).thenReturn(response);
        when(creditRepository.save(credit)).thenReturn(credit);

        CreditResponseDTO result = creditService.saveCreditFromRabbit(message);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void updateByContractNumberTest() {
        String contractNumber = "11-0216444-2-1";
        Credit credit = getCredit(contractNumber);
        UpdateCreditDTO request = getUpdateCreditDTO();
        CreditResponseDTO response = getCreditResponseDTO(credit);

        when(creditRepository.findById(contractNumber)).thenReturn(Optional.of(credit));
        when(creditMapper.toCredit(request, credit)).thenReturn(credit);
        when(creditRepository.save(credit)).thenReturn(credit);
        when(creditMapper.toCreditResponseDTO(credit)).thenReturn(response);

        CreditResponseDTO actual = creditService.updateByContractNumber(contractNumber, request);

        Assertions.assertThat(actual).isEqualTo(response);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionTest() {
        String contractNumber = "11-0216444-2-1";
        UpdateCreditDTO request = getUpdateCreditDTO();
        String expectedMessage = "Credit with contractNumber = %s not found".formatted(contractNumber);

        Exception exception = assertThrows(
                ResourceNotFountException.class, () -> creditService.updateByContractNumber(contractNumber, request));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }


    private Credit getCredit(String contractNumber) {
        return new Credit(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                LocalDate.of(2024, 1, 1), 10000L, 10000L, "BYN",
                LocalDate.of(2024, 12, 10), 22.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private CreateCreditDTO getCreateCreditDTO(String contractNumber) {
        return new CreateCreditDTO(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                LocalDate.of(2024, 1, 1), BigDecimal.valueOf(100),
                BigDecimal.valueOf(100), "BYN",
                LocalDate.of(2024, 12, 10), 22.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private CreditResponseDTO getCreditResponseDTO(Credit credit) {
        return new CreditResponseDTO(credit.getContractNumber(), credit.getCustomerId(),
                credit.getContractStartDate(), BigDecimal.valueOf(credit.getTotalDebt()),
                BigDecimal.valueOf(credit.getCurrentDebt()), credit.getCurrency(),
                credit.getRepaymentDate(), credit.getRate(), credit.getIban(),
                credit.getPossibleRepayment(), credit.getIsClosed(), credit.getCustomerType());
    }

    private UpdateCreditDTO getUpdateCreditDTO() {
        return new UpdateCreditDTO(LocalDate.of(2025, 12, 10), 30.0,
                true, false);
    }
}
