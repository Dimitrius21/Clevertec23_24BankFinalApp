package ru.clevertec.bank.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.testutil.jwt.JwtGenerator;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.bank.product.util.Role;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class CardControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtGenerator jwtGenerator;

    @Test
    void saveTest() throws Exception {
        UUID uuid = UUID.fromString("1a72a000-4b8f-43c5-a889-1ebc6d9dc000");
        String token = jwtGenerator.generateTokenByIdWithRole(uuid, Role.ADMINISTRATOR);
        mockMvc.perform(post("/cards")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getCardRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(getCardResponse())));
        mockMvc.perform(
                delete("/cards/{id}", "AABBCCCDDDDEEEEEEEE01010102"));
    }

    @Test
    void getAllTest() throws Exception {
        UUID uuid = UUID.fromString("1a72a000-4b8f-43c5-a889-1ebc6d9dc000");
        String token = jwtGenerator.generateTokenByIdWithRole(uuid, Role.ADMINISTRATOR);
        mockMvc.perform(get("/cards?page=%d&size=%d".formatted(0, 1))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk());
    }

    @Test
    void getByClientIdTest() throws Exception {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        String token = jwtGenerator.generateTokenByIdWithRole(uuid, Role.ADMINISTRATOR);
        String expected = objectMapper.writeValueAsString(getCardResponseByCustomerId());
        mockMvc.perform(get("/cards/client/{id}", uuid)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void getAllTest_returnEmpty() throws Exception {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        String token = jwtGenerator.generateTokenByIdWithRole(uuid, Role.ADMINISTRATOR);
        String json = "{}";
        mockMvc.perform(get("/cards?page=%d&size=%d".formatted(10, 10))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void deleteByIdTest() throws Exception {
        String cardNumber = "5200000000001092";
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        String token = jwtGenerator.generateTokenByIdWithRole(uuid, Role.SUPER_USER);
        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getCardRequest())));

        mockMvc.perform(delete("/cards/{id}", cardNumber)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(cardNumber));
    }

    private CardRequest getCardRequest() {
        UUID uuid = UUID.fromString("1a72a000-4b8f-43c5-a889-1ebc6d9dc000");
        return new CardRequest("1000000000000000", "1000 0000 0000 0000",
                "AABBCCCDDDDEEEEEEEE01010102", uuid.toString(), "LEGAL", "Mister Holder", "NEW");
    }

    private CardResponse getCardResponse() {
        UUID uuid = UUID.fromString("1a72a000-4b8f-43c5-a889-1ebc6d9dc000");
        return new CardResponse("1000000000000000", "AABBCCCDDDDEEEEEEEE01010102", uuid,
                CustomerType.LEGAL, "Mister Holder", CardStatus.NEW);
    }

    private CardResponse getCardResponseByCustomerId() {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        return new CardResponse("5200000000001092", "AABBCCCDDDDEEEEEEEE01010102", uuid,
                CustomerType.PHYSIC, "Nick Jekson", CardStatus.INACTIVE);
    }

}
