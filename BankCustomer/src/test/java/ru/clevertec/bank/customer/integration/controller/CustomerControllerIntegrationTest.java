package ru.clevertec.bank.customer.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.integration.BaseIntegrationTest;
import ru.clevertec.bank.customer.service.JwtService;
import ru.clevertec.bank.customer.testutil.CustomerRequestTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerResponseTestBuilder;
import ru.clevertec.bank.customer.testutil.CustomerUpdateRequestTestBuilder;
import ru.clevertec.bank.customer.util.Role;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class CustomerControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private static final String ID = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            CustomerResponse response = CustomerResponseTestBuilder.aCustomerResponse().build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(get("/customers/%s".formatted(ID))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 401")
        void testShouldReturnExpectedJsonAndStatus401() throws Exception {
            String message = "Full authentication is required to access this resource";

            mockMvc.perform(get("/customers/%s".formatted(ID)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("401"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected json and status 403")
        void testShouldReturnExpectedJsonAndStatus403() throws Exception {
            String notUserId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc725";
            String message = "With a USER role, you can only view/update your customer";
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.USER);

            mockMvc.perform(get("/customers/%s".formatted(notUserId))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorCode").value("403"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            String wrongId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc111";
            String message = "Customer with id %s is not found".formatted(wrongId);
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);

            mockMvc.perform(get("/customers/%s".formatted(wrongId))
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
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);

            mockMvc.perform(get("/customers?page=%d&size=%d".formatted(0, 1))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].customer_id").value(ID))
                    .andExpect(jsonPath("$.content[0].unp").value("123AB6789"))
                    .andExpect(jsonPath("$.content[0].email").value("HornsAndHooves@example.com"));
        }

        @Test
        @DisplayName("test should return expected json and status 401")
        void testShouldReturnExpectedJsonAndStatus401() throws Exception {
            String message = "Full authentication is required to access this resource";

            mockMvc.perform(get("/customers"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("401"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest()
                    .withEmail("new@email.com")
                    .withCustomerFullName("Newest Newton")
                    .build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/customers")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.customer_id").isNotEmpty())
                    .andExpect(jsonPath("$.email").value(request.email()))
                    .andExpect(jsonPath("$.customer_fullname").value(request.customerFullName()));
        }

        @Test
        @DisplayName("test should return expected json and status 401")
        void testShouldReturnExpectedJsonAndStatus401() throws Exception {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String message = "Full authentication is required to access this resource";

            mockMvc.perform(post("/customers")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("401"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected status 403")
        void testShouldReturnExpectedStatus403() throws Exception {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest().build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.USER);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/customers")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() throws Exception {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest()
                    .withEmail("email")
                    .build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);
            String json = "{\"violations\":[{\"field_name\":\"email\",\"message\":\"must be a well-formed email address\"}]}";

            mockMvc.perform(post("/customers")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class UpdateByIdPutEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest()
                    .withEmail("Johnson@email.com")
                    .withPhoneCode("37529")
                    .withPhoneNumber("6523265")
                    .build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/customers/%s".formatted(ID))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.customer_id").value(ID))
                    .andExpect(jsonPath("$.email").value(request.email()))
                    .andExpect(jsonPath("$.phoneCode").value(request.phoneCode()))
                    .andExpect(jsonPath("$.phoneNumber").value(request.phoneNumber()));
        }

        @Test
        @DisplayName("test should return expected json and status 401")
        void testShouldReturnExpectedJsonAndStatus401() throws Exception {
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String message = "Full authentication is required to access this resource";

            mockMvc.perform(put("/customers/%s".formatted(ID))
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("401"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected json and status 403")
        void testShouldReturnExpectedJsonAndStatus403() throws Exception {
            String wrongId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc722";
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest().build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.USER);
            String content = objectMapper.writeValueAsString(request);
            String message = "With a USER role, you can only view/update your customer";

            mockMvc.perform(put("/customers/%s".formatted(wrongId))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorCode").value("403"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() throws Exception {
            String wrongId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc111";
            String message = "Customer with id %s is not found".formatted(wrongId);
            CustomerUpdateRequest request = CustomerUpdateRequestTestBuilder.aCustomerUpdateRequest().build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/customers/%s".formatted(wrongId))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("404"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() throws Exception {
            CustomerRequest request = CustomerRequestTestBuilder.aCustomerRequest()
                    .withEmail("email")
                    .build();
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.ADMINISTRATOR);
            String content = objectMapper.writeValueAsString(request);
            String json = "{\"violations\":[{\"field_name\":\"email\",\"message\":\"must be a well-formed email address\"}]}";

            mockMvc.perform(put("/customers/%s".formatted(ID))
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class DeleteByIdEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            DeleteResponse response = new DeleteResponse("Customer with id %s was successfully deleted"
                    .formatted(ID));
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.SUPER_USER);
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(delete("/customers/%s".formatted(ID))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 401")
        void testShouldReturnExpectedJsonAndStatus401() throws Exception {
            String message = "Full authentication is required to access this resource";

            mockMvc.perform(delete("/customers/%s".formatted(ID)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("401"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected status 403")
        void testShouldReturnExpectedStatus403() throws Exception {
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.USER);

            mockMvc.perform(delete("/customers/%s".formatted(ID))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            String wrongId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc111";
            String message = "Customer with id %s is not found".formatted(wrongId);
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.SUPER_USER);

            mockMvc.perform(delete("/customers/%s".formatted(wrongId))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("404"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

    }

    @Nested
    class RestoreByIdPatchEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            String id = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc544";
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.SUPER_USER);

            mockMvc.perform(patch("/customers/%s".formatted(id))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.customer_id").value(id));
        }

        @Test
        @DisplayName("test should return expected json and status 401")
        void testShouldReturnExpectedJsonAndStatus401() throws Exception {
            String message = "Full authentication is required to access this resource";

            mockMvc.perform(patch("/customers/%s".formatted(ID)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("401"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

        @Test
        @DisplayName("test should return expected status 403")
        void testShouldReturnExpectedStatus403() throws Exception {
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.USER);

            mockMvc.perform(patch("/customers/%s".formatted(ID))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            String wrongId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc111";
            String message = "Customer with id %s is not found".formatted(wrongId);
            String token = jwtService.generateTokenByIdWithRole(UUID.fromString(ID), Role.SUPER_USER);

            mockMvc.perform(patch("/customers/%s".formatted(wrongId))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("404"))
                    .andExpect(jsonPath("$.errorMessage").value(message));
        }

    }

}
