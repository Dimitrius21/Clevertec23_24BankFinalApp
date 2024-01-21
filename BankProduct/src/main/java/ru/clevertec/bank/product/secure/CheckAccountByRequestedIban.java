package ru.clevertec.bank.product.secure;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.util.ParseRequest;

/**
 * Класс для проверки соответствия имени идентифицированного пользователя с имением пользователя в записи БД для данного id
 */
@Component
@AllArgsConstructor
public class CheckAccountByRequestedIban implements CheckUserInRequest {

    @Autowired
    AccountService accountService;

    private final AuthorizationDecision confirmDecision = new AuthorizationDecision(true);
    private final AuthorizationDecision rejectDecision = new AuthorizationDecision(false);

    /**
     * Метод проверяет совпадение имени идентифицированного пользователя с имением пользователя в записи БД для id запроса
     *
     * @param uuid  идентифицированного пользователя
     * @param request  - объект http запроса
     * @return - объект типа AuthorizationDecision с соответствующим решением соответствия
     */
    public AuthorizationDecision check(String uuid, HttpServletRequest request) {
        String uuidInRequest = ParseRequest.getLastSubString(request);

        if (uuid.equals(uuidInRequest)) {
            return confirmDecision;
        } else {
            return rejectDecision;
        }
    }
}
