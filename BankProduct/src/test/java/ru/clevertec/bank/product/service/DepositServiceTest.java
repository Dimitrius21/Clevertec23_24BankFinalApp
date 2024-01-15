package ru.clevertec.bank.product.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.domain.entity.Deposit;
import ru.clevertec.bank.product.mapper.DepositMapper;
import ru.clevertec.bank.product.repository.DepositRepository;
import ru.clevertec.bank.product.repository.specification.DepositSpecification;
import ru.clevertec.bank.product.testutil.deposit.DepInfoUpdateRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositFilterRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoResponseTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositRabbitPayloadRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositTestBuilder;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @InjectMocks
    private DepositService depositService;
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private DepositMapper depositMapper;
    @Captor
    private ArgumentCaptor<Deposit> depositArgumentCaptor;

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
            Deposit deposit = DepositTestBuilder.aDeposit().build();

            doReturn(Optional.of(deposit))
                    .when(depositRepository)
                    .findById(deposit.getAccIban());
            doReturn(expected)
                    .when(depositMapper)
                    .toDepositInfoResponse(deposit);

            DepositInfoResponse actual = depositService.findByIban(deposit.getAccIban());

            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1 that contains expected value")
        void testShouldReturnListOfSizeOne() {
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            Deposit deposit = DepositTestBuilder.aDeposit().build();
            Page<Deposit> page = new PageImpl<>(List.of(deposit));
            Pageable pageable = PageRequest.of(0, 5);

            doReturn(page)
                    .when(depositRepository)
                    .findAll(pageable);
            doReturn(response)
                    .when(depositMapper)
                    .toDepositInfoResponse(deposit);

            Page<DepositInfoResponse> actual = depositService.findAll(pageable);

            assertThat(actual).hasSize(1)
                    .contains(response);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable pageable = PageRequest.of(2, 5);

            doReturn(Page.empty())
                    .when(depositRepository)
                    .findAll(pageable);

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
            Deposit deposit = DepositTestBuilder.aDeposit().build();
            Page<Deposit> page = new PageImpl<>(List.of(deposit));
            Pageable pageable = PageRequest.of(0, 5);

            doReturn(page)
                    .when(depositRepository)
                    .findAll(any(DepositSpecification.class), any(Pageable.class));
            doReturn(response)
                    .when(depositMapper)
                    .toDepositInfoResponse(deposit);

            Page<DepositInfoResponse> actual = depositService.findAllByFilter(request, pageable);

            assertThat(actual).hasSize(1)
                    .contains(response);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            DepositFilterRequest request = DepositFilterRequestTestBuilder.aDepositFilterRequest().build();
            Pageable pageable = PageRequest.of(2, 5);

            doReturn(Page.empty())
                    .when(depositRepository)
                    .findAll(any(DepositSpecification.class), any(Pageable.class));

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
            Deposit deposit = DepositTestBuilder.aDeposit().build();
            String expectedMessage = "Deposit with such acc_iban is already exist";

            doReturn(Optional.of(deposit))
                    .when(depositRepository)
                    .findById(request.accInfo().accIban());

            Exception exception = assertThrows(RequestBodyIncorrectException.class, () -> depositService.save(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest().build();
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            Deposit deposit = DepositTestBuilder.aDeposit().build();

            doReturn(Optional.empty())
                    .when(depositRepository)
                    .findById(request.accInfo().accIban());
            doReturn(deposit)
                    .when(depositMapper)
                    .toDeposit(request);
            doReturn(deposit)
                    .when(depositRepository)
                    .save(deposit);
            doReturn(response)
                    .when(depositMapper)
                    .toDepositInfoResponse(deposit);

            DepositInfoResponse actual = depositService.save(request);

            assertThat(actual).isEqualTo(response);
        }

        @Test
        @DisplayName("test should save rabbit request")
        void testShouldSaveRabbitRequest() {
            Deposit deposit = DepositTestBuilder.aDeposit().build();
            DepositRabbitPayloadRequest request = DepositRabbitPayloadRequestTestBuilder.aDepositRabbitPayloadRequest().build();

            doReturn(deposit)
                    .when(depositMapper)
                    .toDeposit(request);
            doReturn(deposit)
                    .when(depositRepository)
                    .save(deposit);

            depositService.save(request);
            verify(depositRepository).save(depositArgumentCaptor.capture());

            Deposit captorValue = depositArgumentCaptor.getValue();
            assertThat(captorValue).isEqualTo(deposit);
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
            DepInfoUpdateRequest request = DepInfoUpdateRequestTestBuilder.aDepInfoUpdateRequest().build();
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            Deposit deposit = DepositTestBuilder.aDeposit().build();
            String accIban = "SA0380000000608010167519";

            doReturn(Optional.of(deposit))
                    .when(depositRepository)
                    .findById(accIban);
            doReturn(deposit)
                    .when(depositMapper)
                    .updateDeposit(request, deposit);
            doReturn(deposit)
                    .when(depositRepository)
                    .save(deposit);
            doReturn(response)
                    .when(depositMapper)
                    .toDepositInfoResponse(deposit);

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
            Deposit deposit = DepositTestBuilder.aDeposit().build();
            String accIban = "SA0380000000608010167519";
            DeleteResponse response = new DeleteResponse("Deposit with iban %s was successfully deleted".formatted(accIban));

            doReturn(Optional.of(deposit))
                    .when(depositRepository)
                    .findById(accIban);
            doNothing()
                    .when(depositRepository)
                    .delete(deposit);

            DeleteResponse actual = depositService.deleteByIban(accIban);

            assertThat(actual).isEqualTo(response);
        }

    }

}
