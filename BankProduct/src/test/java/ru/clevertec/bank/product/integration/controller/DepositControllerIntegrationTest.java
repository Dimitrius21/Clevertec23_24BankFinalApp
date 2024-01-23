package ru.clevertec.bank.product.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.integration.BaseIntegrationTest;
import ru.clevertec.bank.product.testutil.deposit.AccInfoRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepInfoRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoRequestTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositInfoResponseTestBuilder;
import ru.clevertec.bank.product.testutil.jwt.JwtGenerator;
import ru.clevertec.bank.product.util.Role;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class DepositControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final JwtGenerator jwtGenerator;
    private final ObjectMapper objectMapper;
    private static final String IBAN = "SA0380000000608010167519";
    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    @Nested
    class FindByIbanGetEndPointTest {

        @Test
        @DisplayName("test should return expected json and status 200 for ADMINISTRATOR")
        void testShouldReturnExpectedJsonAndStatus200ForASMINISTRATOR() throws Exception {
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            String token = jwtGenerator.generateTokenByIdWithRole(response.customerId(), Role.ADMINISTRATOR);
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(get("/deposits/%s".formatted(IBAN))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200 for USER")
        void testShouldReturnExpectedJsonAndStatus200ForUSER() throws Exception {
            DepositInfoResponse response = DepositInfoResponseTestBuilder.aDepositInfoResponse().build();
            String token = jwtGenerator.generateTokenByIdWithRole(response.customerId(), Role.USER);
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(get("/deposits/%s".formatted(IBAN))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            mockMvc.perform(get("/deposits/%s".formatted(IBAN)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(get("/deposits/%s".formatted(IBAN))
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

            mockMvc.perform(get("/deposits/%s".formatted(IBAN))
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

            mockMvc.perform(get("/deposits/%s".formatted(IBAN))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            UUID userId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String wrongIban = "AAAAAAAAAAAAAAAAAAAA";
            String message = "Deposit with iban %s is not found".formatted(wrongIban);
            String token = jwtGenerator.generateTokenByIdWithRole(userId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/deposits/%s".formatted(wrongIban))
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

            mockMvc.perform(get("/deposits?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].acc_info.acc_iban").value("FR7630001007941234567890185"))
                    .andExpect(jsonPath("$.content[0].acc_info.curr_amount").value("10000.0"))
                    .andExpect(jsonPath("$.content[0].dep_info.rate").value("0.05"))
                    .andExpect(jsonPath("$.content[0].customer_type").value("LEGAL"));
        }

        @Test
        @DisplayName("test should return expected empty json and status 200")
        void testShouldReturnExpectedEmptyJsonAndStatus200() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/deposits?page=%d&size=%d".formatted(10, 10))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            mockMvc.perform(get("/deposits?page=%d&size=%d".formatted(0, 1)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(get("/deposits?page=%d&size=%d".formatted(0, 1))
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

            mockMvc.perform(get("/deposits?page=%d&size=%d".formatted(0, 1))
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

            mockMvc.perform(get("/deposits?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 406")
        void testShouldReturnExpectedJsonAndStatus406() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String message = "No property 'exDate' found for type 'DepInfo'; Did you mean 'expDate'; Traversed path: Deposit.depInfo";

            mockMvc.perform(get("/deposits?page=%d&size=%d&sort=%s".formatted(0, 1, "depInfo.exDate"))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(jsonPath("$.errorCode").value("406"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

    }

    @Nested
    class FindAllByFilterGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200 if greaterThan true")
        void testShouldReturnExpectedJsonAndStatus200IfGreaterThanTrue() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/deposits/filter?accIban=%s&greaterThan=%s&amount=%d&currency=%s&page=%d&size=%d"
                            .formatted("FR7630001007941234567890185", "true", 1000, "EUR", 0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].acc_info.acc_iban").value("FR7630001007941234567890185"))
                    .andExpect(jsonPath("$.content[0].acc_info.curr_amount").value("10000.0"))
                    .andExpect(jsonPath("$.content[0].dep_info.rate").value("0.05"))
                    .andExpect(jsonPath("$.content[0].customer_type").value("LEGAL"));
        }

        @Test
        @DisplayName("test should return expected json and status 200 if greaterThan false")
        void testShouldReturnExpectedJsonAndStatus200IfGreaterThanFalse() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/deposits/filter?accIban=%s&greaterThan=%s&amount=%d&currency=%s&page=%d&size=%d"
                            .formatted("FR7630001007941234567890185", "false", 100000, "EUR", 0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].acc_info.acc_iban").value("FR7630001007941234567890185"))
                    .andExpect(jsonPath("$.content[0].acc_info.curr_amount").value("10000.0"))
                    .andExpect(jsonPath("$.content[0].dep_info.rate").value("0.05"))
                    .andExpect(jsonPath("$.content[0].customer_type").value("LEGAL"));
        }

        @Test
        @DisplayName("test should return expected empty json and status 200")
        void testShouldReturnExpectedEmptyJsonAndStatus200() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);

            mockMvc.perform(get("/deposits/filter?accIban=%s&greaterThan=%s&amount=%d&currency=%s&page=%d&size=%d"
                            .formatted("FR7630001007941234567890185", "true", 1000, "EUR", 10, 10))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        @DisplayName("test should return expected header and status 401 if no jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfNoJwtInHeader() throws Exception {
            mockMvc.perform(get("/deposits/filter?page=%d&size=%d".formatted(0, 1)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(get("/deposits/filter?page=%d&size=%d".formatted(0, 1))
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

            mockMvc.perform(get("/deposits/filter?page=%d&size=%d".formatted(0, 1))
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

            mockMvc.perform(get("/deposits/filter?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 406")
        void testShouldReturnExpectedJsonAndStatus406() throws Exception {
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String message = "No property 'exDate' found for type 'DepInfo'; Did you mean 'expDate'; Traversed path: Deposit.depInfo";

            mockMvc.perform(get("/deposits/filter?accIban=%s&greaterThan=%s&amount=%d&currency=%s&page=%d&size=%d&sort=%s"
                            .formatted("FR7630001007941234567890185", "false", 100000, "EUR", 0, 1, "depInfo.exDate"))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(jsonPath("$.errorCode").value("406"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201 with role USER")
        void testShouldReturnExpectedJsonAndStatus201WithRoleUser() throws Exception {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("AABBCCCDDDDEEEEEEEEEEEEEEEEZ")
                            .build())
                    .build();
            String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString(request.customerId()), Role.USER);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/deposits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.customer_id").value(request.customerId()))
                    .andExpect(jsonPath("$.customer_type").value(request.customerType()))
                    .andExpect(jsonPath("$.acc_info.acc_iban").value(request.accInfo().accIban()))
                    .andExpect(jsonPath("$.acc_info.curr_amount_currency").value(request.accInfo().currAmountCurrency()))
                    .andExpect(jsonPath("$.dep_info.rate").value(request.depInfo().rate()))
                    .andExpect(jsonPath("$.dep_info.dep_type").value(request.depInfo().depType()));
        }

        @Test
        @DisplayName("test should return expected json and status 201 with role ADMINISTRATOR")
        void testShouldReturnExpectedJsonAndStatus201WithRoleADMINISTRATOR() throws Exception {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("AABBCCCDDDDEEEEEEEEEEEEEEEEZ")
                            .build())
                    .build();
            String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString(request.customerId()), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/deposits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.customer_id").value(request.customerId()))
                    .andExpect(jsonPath("$.customer_type").value(request.customerType()))
                    .andExpect(jsonPath("$.acc_info.acc_iban").value(request.accInfo().accIban()))
                    .andExpect(jsonPath("$.acc_info.curr_amount_currency").value(request.accInfo().currAmountCurrency()))
                    .andExpect(jsonPath("$.dep_info.rate").value(request.depInfo().rate()))
                    .andExpect(jsonPath("$.dep_info.dep_type").value(request.depInfo().depType()));
        }

        @Test
        @DisplayName("test should return expected json and status 400")
        void testShouldReturnExpectedJsonAndStatus400() throws Exception {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest().build();
            String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString(request.customerId()), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);
            String message = "Deposit with such acc_iban is already exist";

            mockMvc.perform(post("/deposits")
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
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("AABBCCCDDDDEEEEEEEEEEEEEEEEZ")
                            .build())
                    .build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/deposits")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer")));
        }

        @Test
        @DisplayName("test should return expected header and status 401 if bad jwt in header")
        void testShouldReturnExpectedHeaderAndStatus401IfBadJwtInHeader() throws Exception {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("AABBCCCDDDDEEEEEEEEEEEEEEEEZ")
                            .build())
                    .build();
            String content = objectMapper.writeValueAsString(request);
            String token = "Bad token";
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Bearer token is malformed\"";

            mockMvc.perform(post("/deposits")
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
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("AABBCCCDDDDEEEEEEEEEEEEEEEEZ")
                            .build())
                    .build();
            String content = objectMapper.writeValueAsString(request);
            UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
            String token = jwtGenerator.generateTokenByIdWithRole(customerId, Role.ADMINISTRATOR);
            String badSignatureToken = token.substring(1);
            String bearerErrorHeader = "Bearer error=\"invalid_token\"";
            String bearerErrorDescription = "error_description=\"Presented token isn't valid\"";

            mockMvc.perform(post("/deposits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(badSignatureToken)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorHeader)))
                    .andExpect(header().string(WWW_AUTHENTICATE, containsString(bearerErrorDescription)));
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() throws Exception {
            DepositInfoRequest request = DepositInfoRequestTestBuilder.aDepositInfoRequest()
                    .withAccInfo(AccInfoRequestTestBuilder.aAccInfoRequest()
                            .withAccIban("AABBCCCDDDDEEEEEEEEEEEEEEEEZ")
                            .build())
                    .withDepInfo(DepInfoRequestTestBuilder.aDepositInfoRequest()
                            .withTermScale("Z")
                            .build())
                    .build();
            String token = jwtGenerator.generateTokenByIdWithRole(UUID.fromString(request.customerId()), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);
            String json = "{\"violations\":[{\"field_name\":\"depInfo.termScale\",\"message\":\"Acceptable termScales are only: D(days) or M(months)\"}]}";

            mockMvc.perform(post("/deposits")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

}
