package ru.clevertec.bank.currency.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.clevertec.bank.currency.domain.entity.Rate;
import ru.clevertec.bank.currency.domain.entity.Rates;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class CurrencyRateRepositoryTest {
    @Autowired
    CurrencyRateRepository repository;

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("user")
            .withPassword("psw")
            .withDatabaseName("rates_test");

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
    void findFirstByStartBeforeOrderByStartDescTest() {
        ZonedDateTime time = ZonedDateTime.of(2024, 1, 10, 15, 0, 0, 0, ZoneId.systemDefault());
        Rates result = repository.findFirstByStartBeforeOrderByStartDesc(time);
        ZonedDateTime expected = ZonedDateTime.of(2024, 1, 10, 14, 0, 0, 0, ZoneId.systemDefault());
        Assertions.assertThat(result.getStart()).isEqualTo(expected);
        Assertions.assertThat(result.getExchangeRates()).hasSize(3);
    }

    @Test
    void saveTest() {
        Rates rates = getRates();
        Rates result = repository.save(rates);
        Assertions.assertThat(result.getId()).isNotZero();
        Assertions.assertThat(result.getExchangeRates()).hasSize(2);
    }

    private Rates getRates() {
        Rate rateUSD = new Rate(0, 3.12, 3.15, "USD", "BYN");
        Rate rateEUR = new Rate(0, 3.44, 3.45, "EUR", "BYN");
        return new Rates(0, ZonedDateTime.now(), List.of(rateUSD, rateEUR));
    }
}