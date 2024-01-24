package ru.clevertec.bank.product.secure;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.util.ParseRequest;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CheckCustomerForGet implements CheckUserInRequest {

    private final Map<String, GetUuid> uuidGetters;
    private static final AuthorizationDecision CONFIRM_DECISION = new AuthorizationDecision(true);
    private static final AuthorizationDecision REJECT_DECISION = new AuthorizationDecision(false);

    public AuthorizationDecision check(String username, HttpServletRequest request) {
        String entityId = ParseRequest.getLastSubString(request);
        try {
            String entity = ParseRequest.getEntityName(request);
            UUID uuidForRequest = switch (entity) {
                case "account" -> getter("getUuidInAccount").get(entityId);
                case "cards" -> getter("getUuidInCard").get(entityId);
                case "credits" -> getter("getUuidInCredit").get(entityId);
                case "deposits" -> getter("getUuidInDeposit").get(entityId);
                default -> throw new RequestBodyIncorrectException("Unexpected value: " + entity);
            };
            if (username.equals(uuidForRequest.toString())) {
                return CONFIRM_DECISION;
            } else {
                return REJECT_DECISION;
            }
        } catch (ClassNotFoundException ex) {
            throw new InternalServerErrorException("Such entity not support");
        }
    }

    private GetUuid getter(String name) throws ClassNotFoundException {
        String componentName = uuidGetters.keySet().stream()
                .filter(it -> it.contains(name))
                .findFirst()
                .orElseThrow(ClassNotFoundException::new);
        return uuidGetters.get(componentName);
    }

}
