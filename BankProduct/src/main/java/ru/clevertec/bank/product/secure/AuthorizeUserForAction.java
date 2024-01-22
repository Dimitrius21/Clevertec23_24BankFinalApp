package ru.clevertec.bank.product.secure;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@AllArgsConstructor
public final class AuthorizeUserForAction implements AuthorizationManager<RequestAuthorizationContext> {

    private static final AuthorizationDecision CONFIRM_DECISION = new AuthorizationDecision(true);
    private static final AuthorizationDecision REJECT_DECISION = new AuthorizationDecision(false);
    private static final String ADMINISTRATOR = "ROLE_ADMINISTRATOR";

    private CheckUserInRequest checkUser;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            return REJECT_DECISION;
        }
        Authentication auth = authentication.get();
        if (!auth.isAuthenticated()) {
            return REJECT_DECISION;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<String> authoritiesAsString = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        if (authoritiesAsString.contains(ADMINISTRATOR)) {
            return CONFIRM_DECISION;
        }
        return checkUser.check(auth.getName(), request);
    }

}
