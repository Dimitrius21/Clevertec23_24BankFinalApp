package ru.clevertec.bank.customer.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:15.4");

    @BeforeAll
    static void starContainer() {
        CONTAINER.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        CONTAINER.start();
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
    }

}