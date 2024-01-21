package ru.clevertec.bank.product.secure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;

/**
 * Интерфейс определяющий метод для проверки соответствия имени идентифицированного пользователя с имением пользователя в запросе
 */
public interface CheckUserInRequest {
    public AuthorizationDecision check(String username, HttpServletRequest request);
}
