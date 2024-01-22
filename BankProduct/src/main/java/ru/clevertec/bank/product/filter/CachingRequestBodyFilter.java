package ru.clevertec.bank.product.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.bank.product.util.CachedBodyHttpServletRequest;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CachingRequestBodyFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        try {
            filterChain.doFilter(cachedBodyHttpServletRequest, httpServletResponse);
        } catch (ResourceNotFountException e) {
            handleException(httpServletResponse, e);
        }
    }

    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        int status = HttpStatus.NOT_FOUND.value();
        response.setStatus(status);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("utf-8");
        ErrorInfo errorInfo = new ErrorInfo(status, e.getMessage());
        String responseMessage = objectMapper.writeValueAsString(errorInfo);
        log.error(errorInfo.toString());
        response.getWriter().write(responseMessage);
    }

}
