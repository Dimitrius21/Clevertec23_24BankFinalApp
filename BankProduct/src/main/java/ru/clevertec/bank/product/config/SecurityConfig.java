package ru.clevertec.bank.product.config;

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
import ru.clevertec.bank.product.filter.ExceptionFilter;
import ru.clevertec.bank.product.filter.JwtFilter;
import ru.clevertec.bank.product.util.Role;

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
                        .requestMatchers(HttpMethod.POST, "/cards").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.POST, "/credits").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.POST, "/account").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.POST, "/deposits").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.PUT, "/cards/{id}").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/credits/{contractNumber}").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/account").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/deposits/{iban}").hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/cards/{id}").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/credits/{contractNumber}").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/account/{iban}").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/deposits/{iban}").hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/account").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/cards").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/credits").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/deposits").hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/account/customer/{id}").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/cards/client/{id}").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.GET, "/credits/customers/{id}").hasAnyRole(Role.USER.name(), Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/deposits/filter").hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.DELETE, "/account/{iban}").hasRole(Role.SUPER_USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/cards/{id}").hasRole(Role.SUPER_USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/credits/{contractNumber}").hasRole(Role.SUPER_USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/deposits/{iban}").hasRole(Role.SUPER_USER.name())

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
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
