package ru.clevertec.bank.product.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckCustomerForUpdateTest {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private MockHttpServletRequest request;

    private static Map<String, GetUuid> uuidGetters;

    private CheckCustomerForUpdate checkCustomer;

    @Mock
    private GetUuidInAccount getUuidInAccount;
    @Mock
    private GetUuidInCard getUuidInCard;
    @Mock
    private GetUuidInCredit getUuidInCredit;
    @Mock
    private GetUuidInDeposit getUuidInDeposit;

    @BeforeAll
    public static void configureObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setDataOfMockHttpServletRequest() {
        request = new MockHttpServletRequest();
        request.setServerName("www.localhost.com");
        request.setLocalPort(8080);

        uuidGetters = Map.of(
                "getUuidInAccount", getUuidInAccount,
                "getUuidInCard", getUuidInCard,
                "getUuidInCredit", getUuidInCredit,
                "getUuidInDeposit", getUuidInDeposit
        );
        checkCustomer = new CheckCustomerForUpdate(objectMapper, uuidGetters);
    }

    @ParameterizedTest
    @ArgumentsSource(ArgsSupplier.class)
    void check(List<String> args) {
        String uri = args.get(0);
        request.setRequestURI(uri);
        String entity = args.get(2);
        request.setContent(args.get(1).getBytes(StandardCharsets.UTF_8));
        String uuid = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        String iban = "AABBCCDDEE01";
        boolean expected = false;
        if (entity.equals("account") || entity.equals("deposits")) {
            expected = true;
        }
        if (entity.equals("deposits")) {
            when(getUuidInDeposit.get(iban)).thenReturn(UUID.fromString(uuid));
        }
        AuthorizationDecision res = checkCustomer.check(uuid, request);

        Assertions.assertThat(res.isGranted()).isEqualTo(expected);
    }

    @Test
    void checkUnknownEntityTest() {
        String path = "customer";
        request.setRequestURI(String.format("/%s/AABBCCDDEE01", path));

        String uuid = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";

        Assertions.assertThatThrownBy(()-> checkCustomer.check(uuid, request))
                .isInstanceOf(ResourceNotFountException.class);
    }

    private static class ArgsSupplier implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(List.of("/account",
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
                                        }""", "account")),
                    Arguments.of(List.of("/deposits/AABBCCDDEE01", "", "deposits")),
                    Arguments.of(List.of("/cards/AABBCCDDEE01", "", "cards")),
                    Arguments.of(List.of("/credits/AABBCCDDEE01", "", "credits"))
            );
        }
    }
}