package ru.clevertec.bank.customer.domain.entity.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.customer.domain.entity.Customer;
import ru.clevertec.bank.customer.testutil.CustomerTestBuilder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerListenerTest {

    private CustomerListener customerListener;

    @BeforeEach
    void setUp() {
        customerListener = new CustomerListener();
    }

    @Test
    @DisplayName("test prePersist should set customerId")
    void testPrePersistShouldSetCustomerId() {
        Customer customer = CustomerTestBuilder.aCustomer()
                .withCustomerId(null)
                .build();

        customerListener.prePersist(customer);

        assertThat(customer.getCustomerId()).isNotNull();
    }

    @Test
    @DisplayName("test prePersist should set registerDate")
    void testPrePersistShouldSetRegisterDate() {
        Customer customer = CustomerTestBuilder.aCustomer()
                .withRegisterDate(null)
                .build();

        customerListener.prePersist(customer);

        assertThat(customer.getRegisterDate()).isEqualTo(LocalDate.now());
    }

}
