package ru.clevertec.bank.product.secure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.util.ParseRequest;
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;

import java.io.IOException;
import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor
public class CheckCustomerForCreate implements CheckUserInRequest {

    private final ObjectMapper objectMapper;
    private static final AuthorizationDecision CONFIRM_DECISION = new AuthorizationDecision(true);
    private static final AuthorizationDecision REJECT_DECISION = new AuthorizationDecision(false);

    public AuthorizationDecision check(String username, HttpServletRequest request) {
        try {
            String body = new String(request.getInputStream().readAllBytes());
            String entity = ParseRequest.getEntityName(request);
            objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
            UUID uuidForRequest = switch (entity) {
                case "account" -> objectMapper.readValue(body, AccountInDto.class).getCustomerId();
                case "deposits" -> UUID.fromString(objectMapper.readValue(body, DepositInfoRequest.class).customerId());
                case "cards" -> throw new AccessDeniedForRoleException("cards");
                case "credit" -> throw new AccessDeniedForRoleException("credit");
                default -> throw new RequestBodyIncorrectException("Unexpected value: " + entity);
            };
            if (username.equals(uuidForRequest.toString())) {
                return CONFIRM_DECISION;
            } else {
                return REJECT_DECISION;
            }
        }catch (AccessDeniedForRoleException ex){
            log.info("User with UUID ={} try execute CREATE {}", username, ex.getMessage());
            return REJECT_DECISION;
        }catch (JsonProcessingException ex) {
            throw new RequestBodyIncorrectException("Body of request can't be parsed");
        } catch (IOException e) {
            throw new RequestBodyIncorrectException("Error of body reading");
        }
    }

}
