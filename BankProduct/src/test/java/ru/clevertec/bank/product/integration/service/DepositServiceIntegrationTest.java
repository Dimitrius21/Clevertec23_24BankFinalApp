package ru.clevertec.bank.product.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.service.DepositService;
import ru.clevertec.bank.product.testutil.deposit.AccInfoRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.AccInfoResponseTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepInfoResponseTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepInfoUpdateRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositFilterRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoResponseTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositRabbitPayloadRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.RabbitAccInfoRequestTestBuilder;
import ru.clevertec.bank.product.util.DepositType;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
public class DepositServiceIntegrationTest extends BaseIntegrationTest {

    private final DepositService depositService;

    @Nested
    class FindByIbanTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            String iban = "ABCDEFGHTY";
            String expectedMessage = "Deposit with iban %s is not found".formatted(iban);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> depositService.findByIban(iban));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            DepositInfoResponse expected = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            String iban = "SA0380000000608010167519";

            DepositInfoResponse actual = depositService.findByIban(iban);

            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1 that contains expected value")
        void testShouldReturnListOfSizeOne() {
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            Pageable pageable = PageRequest.of(3, 1);

            Page<DepositInfoResponse> actual = depositService.findAll(pageable);

            assertThat(actual).hasSize(1)
                    .contains(response);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable pageable = PageRequest.of(10, 10);

            Page<DepositInfoResponse> actual = depositService.findAll(pageable);

            assertThat(actual).isEmpty();
        }

    }

    @Nested
    class FindAllByFilterTest {

        @Test
        @DisplayName("test should return List of size 1 that contains expected value")
        void testShouldReturnListOfSizeOne() {
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            DepositFilterRequest request = DepositFilterRequestTestBuilder.aDepositFilterRequest().build();
            Pageable pageable = PageRequest.of(0, 1);

            Page<DepositInfoResponse> actual = depositService.findAllByFilter(request, pageable);

            assertThat(actual).hasSize(1)
                    .contains(response);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            DepositFilterRequest request = DepositFilterRequestTestBuilder.aDepositFilterRequest().build();
            Pageable pageable = PageRequest.of(10, 10);

            Page<DepositInfoResponse> actual = depositService.findAllByFilter(request, pageable);

            assertThat(actual).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @Test
        @DisplayName("test should throw RequestBodyIncorrectException with expected message")
        void testShouldThrowRequestBodyIncorrectException() {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest().build();
            String expectedMessage = "Deposit with such acc_iban is already exist";

            Exception exception = assertThrows(RequestBodyIncorrectException.class, () -> depositService.save(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withCustomerId("1a72a05f-4b8f-43c5-a889-1ebc6d9db351")
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("JOHNNYWICK")
                            .build())
                    .build();
            LocalDate now = LocalDate.now();
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse()
                    .withCustomerId(UUID.fromString(request.customerId()))
                    .withAccInfo(AccInfoResponseTestBuilder.aAccInfoResponse()
                            .withAccIban(request.accInfo().accIban())
                            .withAccOpenDate(now)
                            .build())
                    .withDepInfo(DepInfoResponseTestBuilder.aDepInfoResponse()
                            .withExpDate(now.plusDays(1))
                            .build())
                    .build();

            DepositInfoResponse actual = depositService.save(request);

            assertThat(actual).isEqualTo(response);
        }

        @Test
        @DisplayName("test should save rabbit request")
        void testShouldSaveRabbitRequest() {
            DepositRabbitPayloadRequest request = DepositRabbitPayloadRequestTestBuilder.aDepositRabbitPayloadRequest()
                    .withCustomerId("1a72a05f-4b8f-43c5-a889-1ebc6d9dc789")
                    .withAccInfo(RabbitAccInfoRequestTestBuilder.aRabbitAccInfoRequest()
                            .withAccIban("STRANGERTHINGS")
                            .build())
                    .build();

            depositService.save(request);

            DepositInfoResponse response = depositService.findByIban(request.accInfo().accIban());

            assertThat(response.accInfo().accIban()).isEqualTo(request.accInfo().accIban());
            assertThat(response.customerId()).isEqualTo(UUID.fromString(request.customerId()));
        }

    }

    @Nested
    class UpdateByIbanTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            DepInfoUpdateRequest request = DepInfoUpdateRequestTestBuilder.aDepInfoUpdateRequest().build();
            String iban = "ABCDEFGHTY";
            String expectedMessage = "Deposit with iban %s is not found".formatted(iban);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> depositService.updateByIban(iban, request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            DepInfoUpdateRequest request = DepInfoUpdateRequestTestBuilder.aDepInfoUpdateRequest()
                    .withDepType("REVOCABLE")
                    .withAutoRenew(true)
                    .build();
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse()
                    .withDepInfo(DepInfoResponseTestBuilder.aDepInfoResponse()
                            .withDepType(DepositType.valueOf(request.depType()))
                            .withAutoRenew(request.autoRenew())
                            .build())
                    .build();
            String accIban = "SA0380000000608010167519";

            DepositInfoResponse actual = depositService.updateByIban(accIban, request);

            assertThat(actual).isEqualTo(response);
        }

    }

    @Nested
    class DeleteByIbanTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            String iban = "ABCDEFGHTY";
            String expectedMessage = "Deposit with iban %s is not found".formatted(iban);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> depositService.deleteByIban(iban));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            String accIban = "CA2600000000000000000";
            DeleteResponse response = new DeleteResponse("Deposit with iban %s was successfully deleted".formatted(accIban));

            DeleteResponse actual = depositService.deleteByIban(accIban);

            assertThat(actual).isEqualTo(response);
        }

    }

}
