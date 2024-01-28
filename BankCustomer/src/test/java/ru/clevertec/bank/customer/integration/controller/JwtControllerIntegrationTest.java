package ru.clevertec.bank.customer.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.customer.domain.dto.JwtRequest;
import ru.clevertec.bank.customer.integration.BaseIntegrationTest;
import ru.clevertec.bank.customer.testutil.JwtRequestTestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class JwtControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Nested
    class GenerateJwtPostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/jwt")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.jwt").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() throws Exception {
            JwtRequest request = JwtRequestTestBuilder.aJwtRequest()
                    .withRole("ZERO")
                    .build();
            String content = objectMapper.writeValueAsString(request);
            String json = "{\"violations\":[{\"field_name\":\"role\",\"message\":\"Acceptable roles are only:" +
                          " USER, ADMINISTRATOR or SUPER_USER\"}]}";

            mockMvc.perform(post("/jwt")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

}
