package ru.clevertec.bank.product.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.testutil.jwt.JwtGenerator;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.bank.product.util.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class CreditControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final JwtGenerator jwtGenerator;
    private final ObjectMapper objectMapper;
    private final CreditMapper creditMapper;
    private static final String contractNumber = "20-0216444-2-0";
    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    @Nested
    class FindByContractNumberGetEndPointTest {
        @ParameterizedTest(name = "{arguments} test")
        @MethodSource("ru.clevertec.bank.product.integration.controller.CreditControllerIntegrationTest#getArgumentsForRoleTest")
        void testShouldReturnExpectedJsonAndStatus200(Role role) throws Exception {
            Credit credit = getCredit(contractNumber);
            CreditResponseDTO response = creditMapper.toCreditResponseDTO(credit);
            String token = jwtGenerator.generateTokenByIdWithRole(response.getCustomerId(), role);

            mockMvc.perform(get("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            mockMvc.perform(get("/credits/%s".formatted(contractNumber)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(get("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad signature jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadSignatureJwtInHeader() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(get("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 403 for role USER")
        void testShouldReturnExpectedHeaderAndStatus403() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.USER);
            String bearerErrorHeader = "Bearer error=\"insufficient_scope\"";
            String bearerErrorDescription = "error_description=\"The request requires higher privileges than provided" +
                    " by the access token.\"";

            mockMvc.perform(get("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String wrongContactNumber = "AAAAAAAAAAAAAAAAAAAA";
            String message = "Credit with contractNumber = %s not found".formatted(wrongContactNumber);
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/credits/%s".formatted(wrongContactNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("404"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }
    }


    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/credits?page=%d&size=%d".formatted(0, 3))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(3));
        }

        @Test
        @DisplayName("test should return expected empty json and status 200")
        void testShouldReturnExpectedEmptyJsonAndStatus200() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/credits?page=%d&size=%d".formatted(10, 10))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            mockMvc.perform(get("/credits?page=%d&size=%d".formatted(0, 1)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(get("/credits?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad signature jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadSignatureJwtInHeader() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(get("/credits?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 403 for role USER")
        void testShouldReturnExpectedHeaderAndStatus403() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.USER);
            String bearerErrorHeader = "Bearer error=\"insufficient_scope\"";
            String bearerErrorDescription = "error_description=\"The request requires higher privileges than provided" +
                    " by the access token.\"";

            mockMvc.perform(get("/credits?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }
    }


    @Nested
    class FindAllByCustomerIdGetEndpointTest {
        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            Credit credit = getCredit(contractNumber);
            String customerId = credit.getCustomerId().toString();
            String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString(customerId), Role.ADMINISTRATOR);
            List<CreditResponseDTO> creditResponseList = List.of(creditMapper.toCreditResponseDTO(credit));

            mockMvc.perform(get("/credits/customers/{id}", customerId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(content().json(objectMapper.writeValueAsString(creditResponseList)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            mockMvc.perform(get("/credits/customers/{id}", customerId))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad signature jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadSignatureJwtInHeader() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(get("/credits/customers/{id}", customerId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 403 for role USER")
        void testShouldReturnExpectedHeaderAndStatus403() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.USER);
            String bearerErrorHeader = "Bearer error=\"insufficient_scope\"";
            String bearerErrorDescription = "error_description=\"The request requires higher privileges than provided" +
                    " by the access token.\"";

            mockMvc.perform(get("/credits/customers/{id}", userId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }
    }

    @Nested
    class SavePostEndpointTest {

        @ParameterizedTest(name = "{arguments} test")
        @MethodSource("ru.clevertec.bank.product.integration.controller.CreditControllerIntegrationTest#getArgumentsForRoleTest")
        @DisplayName("test should return expected json and status 201 with role USER and ADMINISTRATOR")
        void testShouldReturnExpectedJsonAndStatus201WithRoleUserAndAdministrator(Role role) throws Exception {
            String contractNumber = "99-0777444-2-1";
            Credit credit = getCredit(contractNumber);
            CreateCreditDTO request = getCreateCreditDTO(contractNumber);
            CreditResponseDTO response = creditMapper.toCreditResponseDTO(credit);
            String token = jwtGenerator.generateTokenByIdWithRole((request.getCustomerId()), role);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/credits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        @DisplayName("test should return expected json and status 400")
        void testShouldReturnExpectedJsonAndStatus400() throws Exception {
            CreateCreditDTO request = getCreateCreditDTO(contractNumber);
            String token = jwtGenerator.generateTokenByIdWithRole((request.getCustomerId()), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);
            String message = "Credit with such contractNumber already exist";

            mockMvc.perform(post("/credits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("400"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            String contractNumber = "99-0777444-2-1";
            CreateCreditDTO request = getCreateCreditDTO(contractNumber);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/credits")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String contractNumber = "99-0777444-2-1";
            CreateCreditDTO request = getCreateCreditDTO(contractNumber);
            String content = objectMapper.writeValueAsString(request);
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(post("/credits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad signature jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadSignatureJwtInHeader() throws Exception {
            String contractNumber = "99-0777444-2-1";
            CreateCreditDTO request = getCreateCreditDTO(contractNumber);
            String content = objectMapper.writeValueAsString(request);
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(post("/credits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

    }

    @Nested
    class UpdateByContractNumberPutEndpointTest {

        @ParameterizedTest(name = "{arguments} test")
        @MethodSource("ru.clevertec.bank.product.integration.controller.CreditControllerIntegrationTest#getArgumentsForRoleTest")
        @DisplayName("test should return expected json and status 201 with role USER and ADMINISTRATOR")
        void testShouldReturnExpectedJsonAndStatus201WithRoleUSERandADMINISTRATOR(Role role) throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            UpdateCreditDTO request = getUpdateCreditDTO();
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, role);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/credits/%s".formatted(contractNumber))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rate").value(request.getRate()))
                    .andExpect(jsonPath("$.isClosed").value(request.getIsClosed()))
                    .andExpect(jsonPath("$.possibleRepayment").value(request.getPossibleRepayment()));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            UpdateCreditDTO request = getUpdateCreditDTO();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/credits/%s".formatted(contractNumber))
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            UpdateCreditDTO request = getUpdateCreditDTO();
            String content = objectMapper.writeValueAsString(request);
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(put("/credits/%s".formatted(contractNumber))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad signature jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadSignatureJwtInHeader() throws Exception {
            UpdateCreditDTO request = getUpdateCreditDTO();
            String content = objectMapper.writeValueAsString(request);
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(put("/credits/%s".formatted(contractNumber))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 403 for role USER")
        void testShouldReturnExpectedHeaderAndStatus403() throws Exception {
            UpdateCreditDTO request = getUpdateCreditDTO();
            String content = objectMapper.writeValueAsString(request);
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.USER);
            String bearerErrorHeader = "Bearer error=\"insufficient_scope\"";
            String bearerErrorDescription = "error_description=\"The request requires higher privileges than provided" +
                    " by the access token.\"";

            mockMvc.perform(put("/credits/%s".formatted(contractNumber))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            UpdateCreditDTO request = getUpdateCreditDTO();
            String content = objectMapper.writeValueAsString(request);
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String wrongContactNumber = "AAAAAAAAAAAAAAAAAAAA";
            String message = "Credit with contractNumber = %s not found".formatted(wrongContactNumber);
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.ADMINISTRATOR);

            mockMvc.perform(put("/credits/%s".formatted(wrongContactNumber))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("404"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }
    }


    @Nested
    class DeleteByContractNumberEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            DeleteResponse response = new DeleteResponse("Credit with contractNumber %s was successfully deleted"
                    .formatted(contractNumber));
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.SUPER_USER);
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(delete("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            mockMvc.perform(delete("/credits/%s".formatted(contractNumber)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(delete("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad signature jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadSignatureJwtInHeader() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(delete("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should return expected header and status 403 for role USER and ADMINISTRATOR")
        @MethodSource("ru.clevertec.bank.product.integration.controller.DepositControllerIntegrationTest#getArgumentsForRoleTest")
        void testShouldReturnExpectedHeaderAndStatus403(Role role) throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(userId, role);
            String bearerErrorHeader = "Bearer error=\"insufficient_scope\"";
            String bearerErrorDescription = "error_description=\"The request requires higher privileges than provided" +
                    " by the access token.\"";

            mockMvc.perform(delete("/credits/%s".formatted(contractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String wrongContractNumber = "AAAAAAAAAAAAAAAAAAAA";
            String message = "Credit with contractNumber = %s not found".formatted(wrongContractNumber);
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.SUPER_USER);

            mockMvc.perform(delete("/credits/%s".formatted(wrongContractNumber))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("404"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

    }


    private Credit getCredit(String contractNumber) {
        return new Credit(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721"),
                LocalDate.of(2022, 1, 1), 811399L, 36199L, "BYN",
                LocalDate.of(2025, 1, 1), 18.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private CreateCreditDTO getCreateCreditDTO(String contractNumber) {
        return new CreateCreditDTO(contractNumber, UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721"),
                LocalDate.of(2022, 1, 1), BigDecimal.valueOf(8113.99),
                BigDecimal.valueOf(361.99), "BYN",
                LocalDate.of(2025, 1, 1), 18.8, "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                true, false, CustomerType.LEGAL);
    }

    private UpdateCreditDTO getUpdateCreditDTO() {
        return new UpdateCreditDTO(LocalDate.of(2025, 12, 10), 30.0, true,
                false);
    }

    private static Stream<Arguments> getArgumentsForRoleTest() {
        return Stream.of(
                Arguments.of(Role.USER),
                Arguments.of(Role.ADMINISTRATOR));
    }
}

