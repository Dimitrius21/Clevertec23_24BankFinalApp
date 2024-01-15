package ru.clevertec.bank.customer.filter;

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
import ru.clevertec.bank.customer.service.CustomerService;
import ru.clevertec.bank.customer.service.JwtService;
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerService customerService;

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
        if (Boolean.TRUE.equals(jwtService.isTokenValid(jwt, id)) && customerService.existsByCustomerId(id)) {
            String role = jwtService.extractRole(jwt);
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            if (("GET".equals(method) || "PUT".equals(method)) && requestURI.matches("^/customers/.+$")) {
                UUID requestId = UUID.fromString(requestURI.substring(11));
                if (role.equals("USER") && !requestId.equals(id)) {
                    throw new AccessDeniedForRoleException("With a %s role, you can only view/update your customer".formatted(role));
                }
            }
            User user = new User(id.toString(), "", List.of(new SimpleGrantedAuthority("ROLE_%s".formatted(role))));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

}
