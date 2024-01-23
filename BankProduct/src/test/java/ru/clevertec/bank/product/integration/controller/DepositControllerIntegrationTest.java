package ru.clevertec.bank.product.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoResponseTestBuilder;
import ru.clevertec.bank.product.testutil.jwt.JwtGenerator;
import ru.clevertec.bank.product.util.Role;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class DepositControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final JwtGenerator jwtGenerator;
    private final ObjectMapper objectMapper;
    private static final String IBAN = "SA0380000000608010167519";

    @Nested
    class FindByIbanGetEndPointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            String token = jwtGenerator.generateTokenByIdWithRole(response.customerId(), Role.ADMINISTRATOR);
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(get("/deposits/%s".formatted(IBAN))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

    }

}
