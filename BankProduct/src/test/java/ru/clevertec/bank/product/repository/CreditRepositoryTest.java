package ru.clevertec.bank.product.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class CreditRepositoryTest {

    @Autowired
    CreditRepository creditRepository;

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
        String contractNumber = "11-0216444-2-0";

        Optional<Credit> optionalCredit = creditRepository.findById(contractNumber);

        assertTrue(optionalCredit.isPresent());
        optionalCredit.ifPresent(credit -> assertThat(credit.getContractNumber()).isEqualTo(contractNumber));
    }

    @Test
    void findAllByCustomerIdTest() {
        UUID customer_id = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");

        List<Credit> result = creditRepository.findAllByCustomerId(customer_id);

        assertThat(result).hasSize(3);
        assertThat(result.stream().
                filter(credit -> credit.getCustomerId().equals(customer_id))
                .count()).isEqualTo(3);
    }

    @Test
    void findAllTest() {
        Sort sort = Sort.by(Sort.Order.desc("rate"));
        PageRequest pageable = PageRequest.of(0, 2, sort);

        List<Credit> result = creditRepository.findAll(pageable).toList();

        assertThat(result)
                .hasSize(2)
                .flatExtracting(Credit::getRate)
                .containsExactly(25.8, 22.8);
    }

    @Test
    void saveTest() {
        Credit credit = getCredit();

        Credit result = creditRepository.save(credit);

        assertThat(result).isEqualTo(credit);
    }

    @Test
    void deleteByIdTest() {
        Credit credit = getCredit();
        creditRepository.save(credit);
        String contractNumber = credit.getContractNumber();

        creditRepository.deleteById(contractNumber);

        Optional<Credit> result = creditRepository.findById(contractNumber);
        assertThat(result.isEmpty()).isTrue();
    }


    private Credit getCredit() {
        return new Credit("99-0278944-2-0", UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"),
                LocalDate.of(2024, 1, 1), 100000L, 6780L, "BYN",
                LocalDate.of(2024, 12, 10), 22.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }
}
