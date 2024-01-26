package ru.clevertec.bank.product.secure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckCustomerForGetTest {

    private MockHttpServletRequest request;

    private static Map<String, GetUuid> uuidGetters;

    private CheckCustomerForGet checkCustomer;

    @Mock
    private GetUuidInAccount getUuidInAccount;
    @Mock
    private GetUuidInCard getUuidInCard;
    @Mock
    private GetUuidInCredit getUuidInCredit;
    @Mock
    private GetUuidInDeposit getUuidInDeposit;

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

        checkCustomer = new CheckCustomerForGet(uuidGetters);
    }

    @ParameterizedTest
    @ValueSource(strings = {"account", "cards", "credits", "deposits"})
    void checkTest(String path) {
        request.setRequestURI(String.format("/%s/AABBCCDDEE01", path));
        String iban = "AABBCCDDEE01";
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        switch (path) {
            case "account": {
                when(getUuidInAccount.get(iban)).thenReturn(uuid);
                break;
            }
            case "cards": {
                when(getUuidInCard.get(iban)).thenReturn(uuid);
                break;
            }
            case "credits": {
                when(getUuidInCredit.get(iban)).thenReturn(uuid);
                break;
            }
            case "deposits": {
                when(getUuidInDeposit.get(iban)).thenReturn(uuid);
                break;
            }
        }

        AuthorizationDecision res = checkCustomer.check(uuid.toString(), request);

        Assertions.assertThat(res.isGranted()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"account", "cards", "credits", "deposits"})
    void checkDifferentCustomerIdTest(String path) {
        request.setRequestURI(String.format("/%s/AABBCCDDEE01", path));

        String iban = "AABBCCDDEE01";
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        switch (path) {
            case "account": {
                when(getUuidInAccount.get(iban)).thenReturn(uuid);
                break;
            }
            case "cards": {
                when(getUuidInCard.get(iban)).thenReturn(uuid);
                break;
            }
            case "credits": {
                when(getUuidInCredit.get(iban)).thenReturn(uuid);
                break;
            }
            case "deposits": {
                when(getUuidInDeposit.get(iban)).thenReturn(uuid);
                break;
            }
        }
        CheckCustomerForGet checkCustomer = new CheckCustomerForGet(uuidGetters);
        AuthorizationDecision res = checkCustomer.check("1a72a05f-4b8f-43c5-a889-1ebc6d9dc730", request);

        Assertions.assertThat(res.isGranted()).isFalse();
    }

    @Test
    void checkUnknownEntityTest() {
        String path = "customer";
        request.setRequestURI(String.format("/%s/AABBCCDDEE01", path));

        String iban = "AABBCCDDEE01";
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        switch (path) {
            case "account": {
                when(getUuidInAccount.get(iban)).thenReturn(uuid);
                break;
            }
            case "cards": {
                when(getUuidInCard.get(iban)).thenReturn(uuid);
                break;
            }
            case "credits": {
                when(getUuidInCredit.get(iban)).thenReturn(uuid);
                break;
            }
            case "deposits": {
                when(getUuidInDeposit.get(iban)).thenReturn(uuid);
                break;
            }
        }

        Assertions.assertThatThrownBy(()-> checkCustomer.check(uuid.toString(), request))
                .isInstanceOf(ResourceNotFountException.class);
    }
}