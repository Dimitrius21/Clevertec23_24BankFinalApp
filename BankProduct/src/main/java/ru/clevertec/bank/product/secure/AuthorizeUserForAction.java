package ru.clevertec.bank.product.secure;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Класс для проверки прав идентифицированного пользователя на действия для полученного запроса
 */
@AllArgsConstructor
public final class AuthorizeUserForAction implements AuthorizationManager<RequestAuthorizationContext> {

    private static final AuthorizationDecision CONFIRM_DECISION = new AuthorizationDecision(true);
    private static final AuthorizationDecision REJECT_DECISION = new AuthorizationDecision(false);

    private CheckUserInRequest checkUser;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<String> authoritiesAsString = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (!auth.isAuthenticated()) {
            return REJECT_DECISION;
        }
        if (authoritiesAsString.contains("ROLE_ADMINISTRATOR")) {
            return CONFIRM_DECISION;
        }
        HttpServletRequest req = object.getRequest();
/*        String correspondedRoleForRequestEntity = ParseRequest.getCorrespondedRole(req);
        //Check the role in the request for an action with the correspondent entity
        if (!authoritiesAsString.contains(correspondedRoleForRequestEntity)) {
            return REJECT_DECISION;
        }*/
        return checkUser.check(auth.getName(), req);
    }
}
