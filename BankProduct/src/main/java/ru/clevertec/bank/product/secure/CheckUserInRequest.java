package ru.clevertec.bank.product.secure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;

public interface CheckUserInRequest {

    AuthorizationDecision check(String username, HttpServletRequest request);

}
