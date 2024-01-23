package ru.clevertec.bank.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.util.CustomerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class CreditControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreditMapper creditMapper;

    @Test
    void findByContractNumberTest() throws Exception {
        String contractNumber = "20-0216444-2-0";
        Credit credit = getCredit(contractNumber);
        CreditResponseDTO response = creditMapper.toCreditResponseDTO(credit);

        mockMvc.perform(
                        get("/credits/{contractNumber}", contractNumber))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void findALlTest() throws Exception {
        mockMvc.perform(get("/credits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void findAllByCustomerIdTest() throws Exception {
        String contractNumber = "20-0216444-2-0";
        Credit credit = getCredit(contractNumber);
        String uuid = credit.getCustomerId().toString();
        List<CreditResponseDTO> creditResponseList = List.of(creditMapper.toCreditResponseDTO(credit));

        mockMvc.perform(
                        get("/credits/customers/{id}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(creditResponseList)));
    }

    @Test
    void saveCreditTest() throws Exception {
        String contractNumber = "20-0777444-2-1";
        Credit credit = getCredit(contractNumber);
        CreateCreditDTO dto = getCreateCreditDTO(contractNumber);
        CreditResponseDTO outDto = creditMapper.toCreditResponseDTO(credit);

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));

        mockMvc.perform(
                delete("/credits/{contractNumber}", contractNumber));
    }

    @Test
    void updateByContractNumberTest() throws Exception {
        String contractNumber = "20-1122337-2-9";
        Credit credit = getCredit(contractNumber);
        CreateCreditDTO dto = getCreateCreditDTO(contractNumber);

        mockMvc.perform(
                post("/credits")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto)));

        UpdateCreditDTO updateDto = getUpdateCreditDTO();
        credit.setRate(updateDto.getRate());
        credit.setPossibleRepayment(updateDto.getPossibleRepayment());
        credit.setIsClosed(updateDto.getIsClosed());
        credit.setRepaymentDate(updateDto.getRepaymentDate());
        CreditResponseDTO outDto = creditMapper.toCreditResponseDTO(credit);

        mockMvc.perform(
                        put("/credits/{contractNumber}", contractNumber)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outDto)));

        mockMvc.perform(
                delete("/credits/{contractNumber}", contractNumber));
    }

    @Test
    void deleteByContractNumberTest() throws Exception {
        String contractNumber = "20-1234567-2-1";
        CreateCreditDTO dto = getCreateCreditDTO(contractNumber);

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                        delete("/credits/{contractNumber}", contractNumber))
                .andExpect(status().isOk());
    }


    private Credit getCredit(String contractNumber) {
        return new Credit(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc220"),
                LocalDate.of(2022, 1, 1), 811399L, 36199L, "BYN",
                LocalDate.of(2025, 1, 1), 18.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private CreateCreditDTO getCreateCreditDTO(String contractNumber) {
        return new CreateCreditDTO(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc220"),
                LocalDate.of(2022, 1, 1), BigDecimal.valueOf(8113.99),
                BigDecimal.valueOf(361.99), "BYN",
                LocalDate.of(2025, 1, 1), 18.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private UpdateCreditDTO getUpdateCreditDTO() {
        return new UpdateCreditDTO(LocalDate.of(2025, 12, 10), 30.0, true,
                false);
    }
}
