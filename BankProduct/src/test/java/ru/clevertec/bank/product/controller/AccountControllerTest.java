package ru.clevertec.bank.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.product.mapper.AccountMapper;
import ru.clevertec.bank.product.util.CustomerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@WireMockTest(httpPort = 8090)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountMapper mapper;

    @Test
    void getById() throws Exception {
        String iban = "000000000000000000000000000";
        Account account = getAccount(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);
        mockMvc.perform(
                        get("/account/{iban}", iban))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));
    }

/*    @Test
    void getAllAccountsTest() throws Exception {
        MultiValueMapAdapter requestParams = new MultiValueMapAdapter<>(new HashMap<>());
        requestParams.setAll(Map.of("page", "0", "size", "3"));

        mockMvc.perform(get("/account")
                        .queryParams(requestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }*/

    @Test
    void getByCustomerIdTest() throws Exception {
        String iban = "000000000000000000000000000";
        Account account = getAccount(iban);
        String uuid = account.getCustomerId().toString();
        List<AccountOutDto> outDtoList = List.of(mapper.toAccountOutDto(account));
        mockMvc.perform(
                        get("/account/customer/{id}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(outDtoList)));
    }

    @Test
    void createAccountTest() throws Exception {
        String iban = "000000000000000000000000001";
        Account account = getAccount(iban);
        AccountInDto dto = getInDto(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);

        mockMvc.perform(
                        post("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto)))
                                //.header("Authorization", "Basic TWFyazoyMDA="))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));
        mockMvc.perform(
                delete("/account/{iban}", iban));
    }

    @Test
    void updateAccountTest() throws Exception {
        String iban = "000000000000000000000000001";
        Account account = getAccount(iban);
        AccountInDto dto = getInDto(iban);

        mockMvc.perform(
                        post("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto)));
                //.header("Authorization", "Basic TWFyazoyMDA="))
        dto.setName("Main");
        dto.setRate(1.0);
        account.setName("Main");
        AccountOutDto outDto = mapper.toAccountOutDto(account);

        mockMvc.perform(
                put("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto)))
        //.header("Authorization", "Basic TWFyazoyMDA="))
        .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));

        mockMvc.perform(
                delete("/account/{iban}", iban));
    }

    @Test
    void deleteAccountTest() throws Exception {
        String iban = "000000000000000000000000001";
        AccountInDto dto = getInDto(iban);
        String res = mockMvc.perform(
                        post("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto)))
        //                        .header("Authorization", "Basic TWFyazoyMDA="))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                        delete("/account/{iban}", iban))
//                                .header("Authorization", "Basic TWFyazoyMDA="))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/account/{iban}", iban))
                .andExpect(status().isNotFound());

    }

    private Account getAccount(String iban) {
        return new Account(iban, "Test", 0, "BYN",
                LocalDate.of(2024, 01, 1), true, UUID.fromString("00000000-0000-0000-0000-000000000000"),
                CustomerType.PHYSIC, 0.0, null);
    }

    private AccountInDto getInDto(String iban) {
        return new AccountInDto(iban, "Test", iban, BigDecimal.valueOf(0), 933,
                LocalDate.of(2024, 1, 1), true, UUID.fromString("00000000-0000-0000-0000-000000000000"),
                CustomerType.PHYSIC, 0.0);
    }
}