package ru.clevertec.bank.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;
import ru.clevertec.bank.currency.domain.entity.Rate;
import ru.clevertec.bank.currency.mapper.RatesMapper;
import ru.clevertec.bank.currency.repository.CurrencyRateRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CurrencyRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RatesMapper mapper;

    @Autowired
    private CurrencyRateRepository repo;


    @Test
    void getCurrentTest() throws Exception {
        ZonedDateTime dt = ZonedDateTime.parse("2024-01-11T14:00:00+03");
        String result = mockMvc.perform(
                        get("/rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exchangeRates.length()").value(3))
                .andReturn().getResponse().getContentAsString();

        ZonedDateTime start = objectMapper.readValue(result, RatesOutDto.class).getStart();
        boolean compare = dt.isEqual(start);
        Assertions.assertThat(compare).isTrue();
    }

    @Test
    void getLastForTimeTest() throws Exception {
        ZonedDateTime dt = ZonedDateTime.parse("2024-01-10T16:00:00+03");
        ZonedDateTime expectedDT = ZonedDateTime.parse("2024-01-10T14:00:00+03");
        String result = mockMvc.perform(
                        get("/rate/{time}", dt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exchangeRates.length()").value(3))
                .andReturn().getResponse().getContentAsString();

        ZonedDateTime start = objectMapper.readValue(result, RatesOutDto.class).getStart();
        boolean compare = expectedDT.isEqual(start);
        Assertions.assertThat(compare).isTrue();
    }

    @Test
    void createTest() throws Exception {
        Matcher<Long> matcher = IsNot.not(0L);
        ZonedDateTime dateTime = ZonedDateTime.of(2014, 01, 11, 12, 10, 0, 0, ZoneId.of("GMT+3"));
        RatesInDto inDto = getRatesInDto(dateTime, 0);

        String result = mockMvc.perform(
                        post("/rate")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(inDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcher))
                .andExpect(jsonPath("$.exchangeRates.length()").value(2))
                .andReturn().getResponse().getContentAsString();

        RatesOutDto outDto = objectMapper.readValue(result, RatesOutDto.class);
        boolean compare = dateTime.isEqual(outDto.getStart());
        Assertions.assertThat(compare).isTrue();
        repo.deleteById(outDto.getId());
    }

    private RatesInDto getRatesInDto(ZonedDateTime time, long id) {
        return new RatesInDto(id, time, getRateList(id));
    }

    private List<Rate> getRateList(long id) {
        Rate rateEUR = new Rate(id, 3.44, 3.45, "EUR", "BYN");
        Rate rateUSD = new Rate(id, 3.12, 3.15, "USD", "BYN");
        return List.of(rateEUR, rateUSD);
    }
}