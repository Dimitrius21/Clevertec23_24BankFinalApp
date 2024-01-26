package ru.clevertec.bank.product.secure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import ru.clevertec.bank.product.testutil.jwt.JwtGenerator;

import java.security.Principal;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizeUserForActionTest {

    @Mock
    private CheckUserInRequest checkUser;

    private RequestAuthorizationContext object;

    private Supplier<Authentication> authentication;

    private MockHttpServletRequest request;

    private JwtGenerator jwtGenerator = new JwtGenerator();

    @InjectMocks
    AuthorizeUserForAction authorizeUserForAction; // = new AuthorizeUserForAction(checkUser);


    @BeforeEach
    public void setFields() {
        request = new MockHttpServletRequest();
        object = new RequestAuthorizationContext(request);
    }

    @Test
    void checkTest() {
        String id = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        TestingAuthenticationToken testToken = new TestingAuthenticationToken(getPrincipal(id), null, List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));

        authentication = () -> testToken;
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ");

        AuthorizationDecision decision = authorizeUserForAction.check(authentication, object);

        Assertions.assertThat(decision.isGranted()).isEqualTo(true);
    }

    @Test
    void checkRequestWithoutTokenTest() {
        String id = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        TestingAuthenticationToken testToken = new TestingAuthenticationToken(getPrincipal(id), null, List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));
        testToken.setAuthenticated(true);
        authentication = () -> testToken;

        AuthorizationDecision decision = authorizeUserForAction.check(authentication, object);

        Assertions.assertThat(decision.isGranted()).isEqualTo(false);
    }

    @Test
    void checkNotAuthenticatedTest() {
        String id = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        TestingAuthenticationToken testToken = new TestingAuthenticationToken(getPrincipal(id), null, List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));
        testToken.setAuthenticated(false);
        authentication = () -> testToken;
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ");

        AuthorizationDecision decision = authorizeUserForAction.check(authentication, object);

        Assertions.assertThat(decision.isGranted()).isEqualTo(false);
    }

    @Test
    void checkNotAuthorisedUserTest() {
        String id = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729";
        TestingAuthenticationToken testToken = new TestingAuthenticationToken(getPrincipal(id), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        testToken.setAuthenticated(true);
        authentication = () -> testToken;
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ");

        when(checkUser.check(id, request)).thenReturn(new AuthorizationDecision(false));

        AuthorizationDecision decision = authorizeUserForAction.check(authentication, object);

        Assertions.assertThat(decision.isGranted()).isEqualTo(false);
    }

    private Principal getPrincipal(String userName) {
        return () -> userName;
    }
}