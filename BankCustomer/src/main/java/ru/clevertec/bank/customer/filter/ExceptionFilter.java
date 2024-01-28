package ru.clevertec.bank.customer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException | AccessDeniedForRoleException e) {
            handleException(response, e);
        }
    }

    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        int status = HttpStatus.UNAUTHORIZED.value();
        if (e instanceof AccessDeniedForRoleException) {
            status = HttpStatus.FORBIDDEN.value();
        }
        response.setStatus(status);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("utf-8");
        ErrorInfo errorInfo = new ErrorInfo(status, e.getMessage());
        String responseMessage = objectMapper.writeValueAsString(errorInfo);
        log.error(errorInfo.toString());
        response.getWriter().write(responseMessage);
    }

}
