package ru.clevertec.bank.customer.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.bank.customer.domain.dto.CustomerRabbitPayloadRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.domain.entity.Customer;
import ru.clevertec.bank.customer.integration.BaseIntegrationTest;
import ru.clevertec.bank.customer.service.CustomerService;
import ru.clevertec.bank.customer.testutil.CustomerRabbitPayloadRequestTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerRequestTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerResponseTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerUpdateRequestTestBuilder;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
public class CustomerServiceIntegrationTest extends BaseIntegrationTest {

    private final CustomerService customerService;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc111");
            String expectedMessage = "Customer with id %s is not found".formatted(id);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            CustomerResponse expected = CustomerResponseTestBuilder.aCustomerResponse().build();
            Customer customer = CustomerTestBuilder.aCustomer().build();
            UUID id = customer.getCustomerId();

            CustomerResponse actual = customerService.findById(id);

            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1 that contains expected value")
        void testShouldReturnListOfSizeOne() {
            CustomerResponse response = CustomerResponseTestBuilder.aCustomerResponse().build();
            Pageable pageable = PageRequest.of(0, 1);

            Page<CustomerResponse> actual = customerService.findAll(pageable);

            assertThat(actual).hasSize(1)
                    .contains(response);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable pageable = PageRequest.of(10, 10);

            Page<CustomerResponse> actual = customerService.findAll(pageable);

            assertThat(actual).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest().build();
            CustomerResponseTestBuilder responseTestBuilder = CustomerResponseTestBuilder.aCustomerResponse()
                    .withRegisterDate(LocalDate.now());

            CustomerResponse actual = customerService.save(request);
            CustomerResponse response = responseTestBuilder.withCustomerId(actual.customerId())
                    .build();

            assertThat(actual).isEqualTo(response);
        }

        @Test
        @DisplayName("test should save rabbit request")
        void testShouldSaveRabbitRequest() {
            CustomerRabbitPayloadRequest request = CustomerRabbitPayloadRequestTestBuilder.aCustomerRabbitPayloadRequest()
                    .withCustomerId(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc429"))
                    .withEmail("Big@gmail.com")
                    .build();

            customerService.save(request);

            CustomerResponse response = customerService.findById(request.customerId());

            assertThat(response.customerId()).isEqualTo(request.customerId());
            assertThat(response.email()).isEqualTo(request.email());
        }

    }

    @Nested
    class UpdateByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc111");
            String expectedMessage = "Customer with id %s is not found".formatted(id);
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest().build();

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.updateById(id, request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            CustomerResponse response = CustomerResponseTestBuilder.aCustomerResponse().build();
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest().build();
            Customer customer = CustomerTestBuilder.aCustomer().build();
            UUID id = customer.getCustomerId();

            CustomerResponse actual = customerService.updateById(id, request);

            assertThat(actual).isEqualTo(response);
        }

    }

    @Nested
    class DeleteByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc111");
            String expectedMessage = "Customer with id %s is not found".formatted(id);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.deleteById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            Customer customer = CustomerTestBuilder.aCustomer().build();
            UUID id = customer.getCustomerId();
            DeleteResponse response = new DeleteResponse("Customer with id %s was successfully deleted"
                    .formatted(id));

            DeleteResponse actual = customerService.deleteById(id);

            assertThat(actual).isEqualTo(response);
        }

    }

    @Nested
    class RestoreByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc111");
            String expectedMessage = "Customer with id %s is not found".formatted(id);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.restoreById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc544");

            CustomerResponse actual = customerService.restoreById(id);

            assertThat(actual.customerId()).isEqualTo(id);
        }

    }

    @Nested
    class ExistsByCustomerIdTest {

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727");

            boolean actual = customerService.existsByCustomerId(id);

            assertThat(actual).isTrue();
        }

    }

}
