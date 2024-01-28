package ru.clevertec.bank.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMapAdapter;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.mapper.AccountMapper;
import ru.clevertec.bank.product.testutil.jwt.JwtGenerator;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.bank.product.util.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@RequiredArgsConstructor
class AccountControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AccountMapper mapper;
    private final JwtGenerator jwtGenerator;

    @Test
    void getByIdTest() throws Exception {
        String iban = "AABBCCCDDDDEEEEEEEE01010102";
        Account account = getAccount(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);
        String token = jwtGenerator.generateTokenByIdWithRole(outDto.getCustomerId(), Role.USER);
        mockMvc.perform(
                        get("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));
    }

    @Test
    void getByIdAnotherUserTest() throws Exception {
        String iban = "AABBCCCDDDDEEEEEEEE01010102";
        Account account = getAccount(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727"),
                Role.USER);
        mockMvc.perform(
                        get("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByIdAdministratorTest() throws Exception {
        String iban = "AABBCCCDDDDEEEEEEEE01010102";
        Account account = getAccount(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);
        String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc727"),
                Role.ADMINISTRATOR);
        mockMvc.perform(
                        get("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));
    }


    @Test
    void getAllAccountsForAdministratorTest() throws Exception {
        MultiValueMapAdapter requestParams = new MultiValueMapAdapter<>(new HashMap<>());
        requestParams.setAll(Map.of("page", "1", "size", "2"));
        String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc730"),
                Role.ADMINISTRATOR);
        mockMvc.perform(get("/account")
                        .queryParams(requestParams)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[1].cards.length()").value(1));
    }

    @Test
    void getAllAccountsForUserTest() throws Exception {
        MultiValueMapAdapter requestParams = new MultiValueMapAdapter<>(new HashMap<>());
        requestParams.setAll(Map.of("page", "1", "size", "2"));
        String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc730"),
                Role.USER);
        mockMvc.perform(get("/account")
                        .queryParams(requestParams)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByCustomerIdTest() throws Exception {
        String uuid = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc727";
        String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc730"),
                Role.ADMINISTRATOR);
        mockMvc.perform(
                        get("/account/customer/{id}", uuid)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].iban").value("AABBCCCDDDDEEEEEEEE01010101"));
    }

    @Test
    void createAccountTest() throws Exception {
        String iban = "000000000000000000000000001";
        Account account = getAccount(iban);
        AccountInDto dto = getInDto(iban);
        AccountOutDto outDto = mapper.toAccountOutDto(account);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);
        String tokenSuper = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.SUPER_USER);

        mockMvc.perform(
                        post("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));
        mockMvc.perform(
                delete("/account/{iban}", iban)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(tokenSuper)));
    }

    @Test
    void createAccountExistedInDbTest() throws Exception {
        String iban = "AABBCCCDDDDEEEEEEEE01010102";
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);
        mockMvc.perform(
                        post("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Data with such Iban already exist"));
    }

    @Test
    void updateAccountTest() throws Exception {
        String iban = "000000000000000000000000001";
        Account account = getAccount(iban);
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);
        String tokenSuper = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.SUPER_USER);

        mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)));
        dto.setName("Main");
        dto.setRate(1.0);
        account.setName("Main");
        AccountOutDto outDto = mapper.toAccountOutDto(account);

        mockMvc.perform(
                        put("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));

        mockMvc.perform(
                delete("/account/{iban}", iban)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(tokenSuper)));
    }

    @Test
    void updateAccountNotExistedTest() throws Exception {
        String iban = "000000000000000000000000001";
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);
        String error = String.format("Account with IBAN=%s not found", iban);

        mockMvc.perform(
                        put("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(error));
    }

    @Test
    void updateAccountNotCorrespondedCustomerTest() throws Exception {
        String iban = "AABBCCCDDDDEEEEEEEEEEEEEEE0";
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);

        mockMvc.perform(
                        put("/account")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(dto))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Incorrect customer_id in request"));
    }

    @Test
    void deleteAccountTest() throws Exception {
        String iban = "000000000000000000000000001";
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);
        String tokenSuper = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.SUPER_USER);
        mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)));

        mockMvc.perform(
                        delete("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(tokenSuper)))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteAccountByUserRoleTest() throws Exception {
        String iban = "000000000000000000000000001";
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.USER);
        String tokenSuper = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.SUPER_USER);
        mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)));

        mockMvc.perform(
                        delete("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isForbidden());

        mockMvc.perform(
                delete("/account/{iban}", iban)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(tokenSuper)));
    }

    @Test
    void deleteAccountByAdministratorRoleTest() throws Exception {
        String iban = "000000000000000000000000001";
        AccountInDto dto = getInDto(iban);
        String token = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.ADMINISTRATOR);
        String tokenSuper = jwtGenerator.generateTokenByIdWithRole(dto.getCustomerId(), Role.SUPER_USER);
        mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)));

        mockMvc.perform(
                        delete("/account/{iban}", iban)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                .andExpect(status().isForbidden());

        mockMvc.perform(
                delete("/account/{iban}", iban)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(tokenSuper)));
    }


    private Account getAccount(String iban) {
        return new Account(iban, "Main", 40000, "BYN",
                LocalDate.of(2024, 01, 11), true, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc726"),
                CustomerType.PHYSIC, 0.01, null);
    }

    private AccountInDto getInDto(String iban) {
        return new AccountInDto(iban, "Main", iban, BigDecimal.valueOf(400), 933,
                LocalDate.of(2024, 1, 11), true, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc726"),
                CustomerType.PHYSIC, 0.01);
    }
}