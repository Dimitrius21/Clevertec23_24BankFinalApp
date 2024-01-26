package ru.clevertec.bank.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.entity.Rate;
import ru.clevertec.bank.product.domain.entity.RateFeign;
//import ru.clevertec.bank.product.integration.service.BaseIntegrationTest;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8083)
@ActiveProfiles("test")
class CardControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveTest() throws Exception {
        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getCardRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(getCardResponse())));
        mockMvc.perform(
                delete("/cards/{id}", "AABBCCCDDDDEEEEEEEE01010102"));
    }

//    @Test
//    public void getByIdTest() throws Exception {
//        String id = "5200000000001092";
//        RateFeign rateFeign = new RateFeign(1l, LocalDateTime.of(2023, 12, 22, 13, 55),
//                                            List.of(new Rate(1l, 3.33d, 3.43d, "EUR", "BYN"),
//                                                    new Rate(1l, 3.05d, 3.15d, "USD", "BYN")));
//        String expected = objectMapper.writeValueAsString(getCardResponse());
//        StubMapping stubMapping = stubFor(WireMock.get(urlEqualTo("/rate"))
//                .willReturn(WireMock.aResponse().withBody(objectMapper.writeValueAsString(rateFeign))));
//        stubMapping.getResponse().getBody().contains()
//        mockMvc.perform(get("/cards/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expected));
//    }

    @Test
    public void getAllTest() throws Exception {
        String expected = objectMapper.writeValueAsString(getCardResponseAll());
        MvcResult result = mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        assertThat(actualContent.contains(expected)).isTrue();
    }

    @Test
    public void getByClientIdTest() throws Exception {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        String expected = objectMapper.writeValueAsString(getCardResponseByCustomerId());
        mockMvc.perform(get("/cards/client/{id}", uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    public void getAllTest_returnEmpty() throws Exception {
        String json = "{}";
        PageRequest page = PageRequest.of(5, 5);
        mockMvc.perform(get("/cards", page))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void deleteByIdTest() throws Exception {
        String cardNumber = "1000000000000000";
        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getCardRequest())));

        mockMvc.perform(delete("/cards/{id}", cardNumber))
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
        return new CardResponse("1000000000000000", "AABBCCCDDDDEEEEEEEE01010102", uuid, CustomerType.LEGAL, "Mister Holder", CardStatus.NEW);
    }

    private CardResponse getCardResponseByCustomerId() {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        return new CardResponse("5200000000001092", "AABBCCCDDDDEEEEEEEE01010102", uuid, CustomerType.PHYSIC, "Nick Jekson", CardStatus.INACTIVE);
    }

    private List<CardResponse> getCardResponseAll() {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        CardResponse card0 = new CardResponse("5200000000001099", "AABBCCCDDDDEEEEEEEEEEEEEEE0", uuid, CustomerType.LEGAL, "Jack Nikson", CardStatus.INACTIVE);
        UUID uuidOne = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc730");
        CardResponse card1 = new CardResponse("5200000000001090", "AABBCCCDDDDEEEEEEEEEEEEEEEF", uuidOne, CustomerType.LEGAL, "Tom Cruz", CardStatus.NEW);
        UUID uuidTwo = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc731");
        CardResponse card2 = new CardResponse("5200000000001091", "AABBCCCDDDDEEEEEEEE01010101", uuidTwo, CustomerType.PHYSIC, "Bob Toyz", CardStatus.ACTIVE);
        UUID uuidThree = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc732");
        CardResponse card3 = new CardResponse("5200000000001092", "AABBCCCDDDDEEEEEEEE01010102", uuidThree, CustomerType.PHYSIC, "Nick Jekson", CardStatus.INACTIVE);
        UUID uuidFour = UUID.fromString("00000000-0000-0000-0000-000000000000");
        CardResponse card4 = new CardResponse("0", "000000000000000000000000000", uuidFour, CustomerType.PHYSIC, "Lily Doyson", CardStatus.BLOCKED);
        return List.of(card0, card1, card2, card3, card4);
    }
}