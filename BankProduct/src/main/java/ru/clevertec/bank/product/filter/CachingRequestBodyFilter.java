package ru.clevertec.bank.product.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;
import ru.clevertec.bank.product.util.CachedBodyHttpServletRequest;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Фильтр http-запроса подключающий оболочку(кэширование) для httpServletRequest
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@AllArgsConstructor
@WebFilter(filterName = "CachingRequestBodyFilter", urlPatterns = "/*")
public class CachingRequestBodyFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        try {
            filterChain.doFilter(cachedBodyHttpServletRequest, httpServletResponse);
        } catch (RequestBodyIncorrectException | ResourceNotFountException ex) {
/*            ErrorIfo err = new ErrorIfo(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            String responseBody = objectMapper.writeValueAsString(err);
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(responseBody);*/
        }
    }
}



