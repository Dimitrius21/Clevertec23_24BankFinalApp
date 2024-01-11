package ru.clevertec.bank.customer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.bank.customer.filter.ExceptionFilter;
import ru.clevertec.bank.customer.filter.JwtFilter;
import ru.clevertec.bank.customer.util.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final ExceptionFilter exceptionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/customers/{id}").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/customers").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.POST, "/customers").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/customers/{id}").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/customers/{id}").hasRole(Role.SUPER_USER.name())
                        .requestMatchers(HttpMethod.PATCH, "/customers/{id}").hasRole(Role.SUPER_USER.name())
                        .requestMatchers("/jwt", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) ->
                        exceptionFilter.handleException(response, authException)))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, JwtFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
