package ru.clevertec.bank.product.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    AccountRepository accRepository;

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("user")
            .withPassword("psw")
            .withDatabaseName("test");

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void startContainers() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stopContainers() {
        postgreSQLContainer.stop();
    }

    @Test
    void findByIdTest() {
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEE0";
        Account result = accRepository.findById(iban).get();
        Assertions.assertThat(result.getIban()).isEqualTo(iban);
    }

    @Test
    void findByCustomerIdTest() {
        UUID customer_id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        List<Account> result = accRepository.findByCustomerId(customer_id);
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.stream().
                        filter(ac -> ac.getCustomerId().equals(customer_id))
                        .count())
                .isEqualTo(2);
    }

        @Test
    void findAllTest() {
        Sort sort = Sort.by(Sort.Order.desc("customerId"));
        PageRequest pageable = PageRequest.of(0, 2, sort);
        List<Account> result = accRepository.findAll(pageable).toList();
        Assertions.assertThat(result)
                .hasSize(2)
                .flatExtracting(Account::getCustomerId)
                .containsExactly(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732"),
                        UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc731"));
    }

    @Test
    void saveTest() {
        Account account = getAccount();
        Account result = accRepository.save(account);
        Assertions.assertThat(result).isEqualTo(account);
    }

    @Test
    void deleteByIdTest() {
        Account account = getAccount();
        accRepository.save(account);
        String iban = account.getIban();
        accRepository.deleteById(iban);
        Optional<Account> result = accRepository.findById(iban);
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    private Account getAccount() {
        return new Account("AABBCCCDDDD0000000000000000", "Main", 10000, "BYN",
                LocalDate.of(2024, 01, 10), true, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                CustomerType.LEGAL, 0.01, null);
    }

}