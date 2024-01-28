package ru.clevertec.bank.product.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;


class CheckCustomerForCreateTest {
    private MockHttpServletRequest request;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private CheckCustomerForCreate checkCustomer;

    @BeforeAll
    public static void configureObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setDataOfMockHttpServletRequest() {
        request = new MockHttpServletRequest();
        request.setServerName("www.localhost.com");
        request.setLocalPort(8080);
        checkCustomer = new CheckCustomerForCreate(objectMapper);
    }

    @ParameterizedTest
    @ArgumentsSource(ArgsSupplier.class)
    void checkTest(List<String> args) {
        String entity = args.get(0);
        request.setRequestURI(String.format("/%s/AABBCCDDEE01", entity));
        request.setContent(args.get(1).getBytes(StandardCharsets.UTF_8));
        String uuid = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        boolean expected = entity.equals("account") || entity.equals("deposits");

        AuthorizationDecision res = checkCustomer.check(uuid, request);

        Assertions.assertThat(res.isGranted()).isEqualTo(expected);
    }

    @Test
    void checkUnknownEntityTest() {
        String path = "customer";
        request.setRequestURI(String.format("/%s/AABBCCDDEE01", path));
        String uuid = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        Assertions.assertThatThrownBy(() -> checkCustomer.check(uuid, request))
                .isInstanceOf(ResourceNotFountException.class);
    }

    private static class ArgsSupplier implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(List.of("account",
                            """
                                    {
                                            "name": "Main",
                                            "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                            "iban_readable": "AABB CCC DDDD EEEE EEEE EEEE EEEE",
                                            "amount": 2100.00,
                                            "currency_code": "933",
                                            "open_date": "20.01.2024",
                                            "main_acc": true,
                                            "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                            "customer_type" : "LEGAL",
                                            "rate": 0.01
                                        }""")),
                    Arguments.of(List.of("deposits", """
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customer_type": "LEGAL",
                              "acc_info": {
                                "acc_iban": "AABBCCCDDDDEEEEEEEEEEEEEEEEE",
                                "curr_amount": 3000.00,
                                "curr_amount_currency": "BYN"
                              },
                              "dep_info": {
                                "rate": 14.50,
                                "term_val": 24,
                                "term_scale": "M",
                                "dep_type": "REVOCABLE",
                                "auto_renew": true
                              }
                            }""")),
                    Arguments.of(List.of("cards", "")),
                    Arguments.of(List.of("credits", ""))
            );
        }
    }
}