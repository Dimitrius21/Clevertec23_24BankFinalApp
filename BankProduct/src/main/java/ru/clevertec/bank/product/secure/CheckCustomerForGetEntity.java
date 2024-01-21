package ru.clevertec.bank.product.secure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.util.ParseRequest;
import ru.clevertec.exceptionhandler.exception.GeneralException;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс для проверки соответствия имени идентифицированного пользователя с имением пользователя в теле запроса с передаваемым объектом
 */
@Component
@AllArgsConstructor
public class CheckCustomerForGetEntity implements CheckUserInRequest {


    @Autowired
    Map<String, GetUuid> uuidGetters;

    private final AuthorizationDecision confirmDecision = new AuthorizationDecision(true);
    private final AuthorizationDecision rejectDecision = new AuthorizationDecision(false);

    /**
     * Метод проверяет совпадение имени идентифицированного пользователя с имением пользователя в записи БД для id запроса
     *
     * @param username имя идентифицированного пользователя
     * @param request  - объект http запроса
     * @return - объект типа AuthorizationDecision с соответствующим решением соответствия
     */
    public AuthorizationDecision check(String username, HttpServletRequest request) {
        String body = null;
        String entityId = ParseRequest.getLastSubString(request);
        try {
            String entity = ParseRequest.getEntityName(request);
            UUID uuidForRequest = switch (entity) {
                case "account" -> getter("account").get(username);
                case "cards" -> getter("card").get(username);
                case "credits" -> getter("credit").get(username);
                case "deposits" -> getter("deposit").get(username);
                default -> null;
            };
            if (Objects.isNull(uuidForRequest)) {
                throw new NotValidRequestParametersException("Invalid request parameter");
            }
            if (username.equals(uuidForRequest.toString())) {
                return confirmDecision;
            } else {
                return rejectDecision;
            }
        } catch (ClassNotFoundException ex) {
            throw new GeneralException("Such entity not support");
        }
    }

    private GetUuid getter(String name) throws ClassNotFoundException {
        String componentName = uuidGetters.keySet().stream()
                .filter(it->it.contains(name))
                .findFirst()
                .orElseThrow(ClassNotFoundException::new);
        return uuidGetters.get(componentName);
    }
}