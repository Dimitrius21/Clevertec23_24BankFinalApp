package ru.clevertec.bank.customer.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class CustomerServiceIntegrationTest extends BaseIntegrationTest {

    private final CustomerService customerService;
    @Mock
    private Authentication authentication;
    @Mock
    private User user;


    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw ResourceNotFountException with expected message")
        void testShouldThrowResourceNotFountException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String expectedMessage = "Customer with id %s is not found".formatted(id);

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.findById(id, authentication));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected response")
        void testShouldReturnExpectedResponse() {
            CustomerResponse expected = CustomerResponseTestBuilder.aCustomerResponse().build();
            Customer customer = CustomerTestBuilder.aCustomer().build();
            UUID id = customer.getCustomerId();
            String authority = "ROLE_USER";

            doReturn(user)
                    .when(authentication)
                    .getPrincipal();
            doReturn(id.toString())
                    .when(user)
                    .getUsername();
            doReturn(Collections.singletonList(new SimpleGrantedAuthority(authority)))
                    .when(user)
                    .getAuthorities();

            CustomerResponse actual = customerService.findById(id, authentication);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("test should throw AccessDeniedForRoleException with expected message")
        void testShouldThrowAccessDeniedForRoleException() {
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727");
            UUID wrongId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String authority = "ROLE_USER";
            String expectedMessage = "With a %s, you can only view/update your customer".formatted(authority);

            doReturn(user)
                    .when(authentication)
                    .getPrincipal();
            doReturn(id.toString())
                    .when(user)
                    .getUsername();
            doReturn(Collections.singletonList(new SimpleGrantedAuthority(authority)))
                    .when(user)
                    .getAuthorities();

            Exception exception = assertThrows(AccessDeniedForRoleException.class,
                    () -> customerService.findById(wrongId, authentication));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
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
            String authority = "ROLE_USER";

            customerService.save(request);

            doReturn(user)
                    .when(authentication)
                    .getPrincipal();
            doReturn(request.customerId().toString())
                    .when(user)
                    .getUsername();
            doReturn(Collections.singletonList(new SimpleGrantedAuthority(authority)))
                    .when(user)
                    .getAuthorities();

            CustomerResponse response = customerService.findById(request.customerId(), authentication);

            assertThat(response.customerId()).isEqualTo(request.customerId());
            assertThat(response.email()).isEqualTo(request.email());
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

            Exception exception = assertThrows(ResourceNotFountException.class, () -> customerService.updateById(id, request, authentication));
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
            String authority = "ROLE_USER";

            doReturn(user)
                    .when(authentication)
                    .getPrincipal();
            doReturn(id.toString())
                    .when(user)
                    .getUsername();
            doReturn(Collections.singletonList(new SimpleGrantedAuthority(authority)))
                    .when(user)
                    .getAuthorities();

            CustomerResponse actual = customerService.updateById(id, request, authentication);

            assertThat(actual).isEqualTo(response);
        }

        @Test
        @DisplayName("test should throw AccessDeniedForRoleException with expected message")
        void testShouldThrowAccessDeniedForRoleException() {
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest().build();
            UUID id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727");
            UUID wrongId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String authority = "ROLE_USER";
            String expectedMessage = "With a %s, you can only view/update your customer".formatted(authority);

            doReturn(user)
                    .when(authentication)
                    .getPrincipal();
            doReturn(id.toString())
                    .when(user)
                    .getUsername();
            doReturn(Collections.singletonList(new SimpleGrantedAuthority(authority)))
                    .when(user)
                    .getAuthorities();

            Exception exception = assertThrows(AccessDeniedForRoleException.class,
                    () -> customerService.updateById(wrongId, request, authentication));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
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
