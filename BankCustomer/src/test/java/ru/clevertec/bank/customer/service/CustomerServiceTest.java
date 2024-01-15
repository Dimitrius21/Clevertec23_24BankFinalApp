package ru.clevertec.bank.customer.service;

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
import ru.clevertec.bank.customer.domain.dto.CustomerRabbitPayloadRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.domain.entity.Customer;
import ru.clevertec.bank.customer.mapper.CustomerMapper;
import ru.clevertec.bank.customer.repository.CustomerRepository;
import ru.clevertec.bank.customer.testutil.CustomerRabbitPayloadRequestTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerRequestTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerResponseTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerUpdateRequestTestBuilder;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Captor
    private ArgumentCaptor<Customer> captor;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
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

            doReturn(Optional.of(customer))
                    .when(customerRepository)
                    .findByCustomerIdAndDeletedFalse(id);
            doReturn(expected)
                    .when(customerMapper)
                    .toResponse(customer);

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
            Customer customer = CustomerTestBuilder.aCustomer().build();
            Page<Customer> page = new PageImpl<>(List.of(customer));
            Pageable pageable = PageRequest.of(0, 5);

            doReturn(page)
                    .when(customerRepository)
                    .findAllByDeletedFalse(pageable);
            doReturn(response)
                    .when(customerMapper)
                    .toResponse(customer);

            Page<CustomerResponse> actual = customerService.findAll(pageable);

            assertThat(actual).hasSize(1)
                    .contains(response);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable pageable = PageRequest.of(2, 5);

            doReturn(Page.empty())
                    .when(customerRepository)
                    .findAllByDeletedFalse(pageable);

            Page<CustomerResponse> actual = customerService.findAll(pageable);

            assertThat(actual).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @Test
        @DisplayName("test should throw InternalServerErrorException with expected message")
        void testShouldThrowRequestBodyIncorrectException() {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest().build();
            String expectedMessage = "Cant save customer";

            Exception exception = assertThrows(InternalServerErrorException.class, () -> customerService.save(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest().build();
            CustomerResponse response = CustomerResponseTestBuilder.aCustomerResponse().build();
            Customer customer = CustomerTestBuilder.aCustomer().build();

            doReturn(customer)
                    .when(customerMapper)
                    .toCustomer(request);
            doReturn(customer)
                    .when(customerRepository)
                    .save(customer);
            doReturn(response)
                    .when(customerMapper)
                    .toResponse(customer);

            CustomerResponse actual = customerService.save(request);

            assertThat(actual).isEqualTo(response);
        }

        @Test
        @DisplayName("test should save rabbit request")
        void testShouldSaveRabbitRequest() {
            Customer customer = CustomerTestBuilder.aCustomer().build();
            CustomerRabbitPayloadRequest request = CustomerRabbitPayloadRequestTestBuilder.aCustomerRabbitPayloadRequest().build();

            doReturn(customer)
                    .when(customerMapper)
                    .toCustomer(request);
            doReturn(customer)
                    .when(customerRepository)
                    .save(customer);

            customerService.save(request);
            verify(customerRepository).save(captor.capture());

            Customer captorValue = captor.getValue();
            assertThat(captorValue).isEqualTo(customer);
        }

    }

    @Nested
    class UpdateByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
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

            doReturn(Optional.of(customer))
                    .when(customerRepository)
                    .findByCustomerIdAndDeletedFalse(id);
            doReturn(customer)
                    .when(customerMapper)
                    .updateCustomer(request, customer);
            doReturn(customer)
                    .when(customerRepository)
                    .save(customer);
            doReturn(response)
                    .when(customerMapper)
                    .toResponse(customer);

            CustomerResponse actual = customerService.updateById(id, request);

            assertThat(actual).isEqualTo(response);
        }

    }

    @Nested
    class DeleteByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727");
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

            doReturn(Optional.of(customer))
                    .when(customerRepository)
                    .findByCustomerIdAndDeletedFalse(id);
            customer.setDeleted(true);
            doReturn(customer)
                    .when(customerRepository)
                    .save(customer);

            DeleteResponse actual = customerService.deleteById(id);

            assertThat(actual).isEqualTo(response);
        }

    }

    @Nested
    class RestoreByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727");
            String expectedMessage = "Customer with id %s is not found".formatted(id);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.restoreById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            Customer customer = CustomerTestBuilder.aCustomer().build();
            CustomerResponse response = CustomerResponseTestBuilder.aCustomerResponse().build();
            UUID id = customer.getCustomerId();

            doReturn(Optional.of(customer))
                    .when(customerRepository)
                    .findByCustomerIdAndDeletedTrue(id);
            customer.setDeleted(false);
            doReturn(customer)
                    .when(customerRepository)
                    .save(customer);
            doReturn(response)
                    .when(customerMapper)
                    .toResponse(customer);

            CustomerResponse actual = customerService.restoreById(id);

            assertThat(actual).isEqualTo(response);
        }

    }

    @Nested
    class ExistsByCustomerIdTest {

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727");

            doReturn(true)
                    .when(customerRepository)
                    .existsByCustomerId(id);

            boolean actual = customerService.existsByCustomerId(id);

            assertThat(actual).isTrue();
        }

    }

}
