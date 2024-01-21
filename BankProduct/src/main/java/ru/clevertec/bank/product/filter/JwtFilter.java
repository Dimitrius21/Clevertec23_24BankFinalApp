package ru.clevertec.bank.product.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.bank.product.service.ExistenceService;
import ru.clevertec.bank.product.service.JwtService;
import ru.clevertec.bank.product.util.BankProductType;
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ExistenceService existenceService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        UUID id = jwtService.extractId(jwt);
        String requestURI = request.getRequestURI();
        boolean isExistByCustomerId = isExistByCustomerId(requestURI, id);
        if (Boolean.TRUE.equals(jwtService.isTokenValid(jwt, id)) && isExistByCustomerId) {
            String role = jwtService.extractRole(jwt);
            String method = request.getMethod();
            checkForRoleUserAccessToGetByCustomerIdEndpoint(method, requestURI, role, id);
            User user = new User(id.toString(), "", List.of(new SimpleGrantedAuthority("ROLE_%s".formatted(role))));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isExistByCustomerId(String requestURI, UUID id) {
        BankProductType bankProductType = BankProductType.fromName("/%s".formatted(requestURI.split("/")[1]));
        return switch (bankProductType) {
            case ACCOUNT -> existenceService.existsByCustomerId(id, BankProductType.ACCOUNT);
            case CARD -> existenceService.existsByCustomerId(id, BankProductType.CARD);
            case CREDIT -> existenceService.existsByCustomerId(id, BankProductType.CREDIT);
            case DEPOSIT -> existenceService.existsByCustomerId(id, BankProductType.DEPOSIT);
            case WRONG -> false;
        };
    }

    private void checkForRoleUserAccessToGetByCustomerIdEndpoint(String method, String requestURI, String role, UUID id) {
        if ("GET".equals(method) && requestURI.matches("/(account|cards|credits)/(customer|client|customers)/[\\w-]+")) {
            Pattern pattern = Pattern.compile("/\\w+-\\w+-\\w+-\\w+-\\w+");
            Matcher matcher = pattern.matcher(requestURI);
            String requestId;
            if (matcher.find()) {
                requestId = matcher.group(0).substring(1);
            } else {
                throw new NotValidRequestParametersException("Not valid uuid");
            }
            if (role.equals("USER") && !UUID.fromString(requestId).equals(id)) {
                throw new AccessDeniedForRoleException("With a %s role, you can only view/update your customer".formatted(role));
            }
        }
    }

}
