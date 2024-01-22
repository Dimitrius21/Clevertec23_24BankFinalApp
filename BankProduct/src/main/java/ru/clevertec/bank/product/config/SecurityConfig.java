package ru.clevertec.bank.product.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.bank.product.filter.CachingRequestBodyFilter;
import ru.clevertec.bank.product.secure.AuthorizeUserForAction;
import ru.clevertec.bank.product.secure.CheckUserInRequest;
import ru.clevertec.bank.product.secure.JwtCustomDecoder;
import ru.clevertec.bank.product.util.Role;

import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Map<String, CheckUserInRequest> authorizationCheckers;
    private final CachingRequestBodyFilter cachingRequestBodyFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        .requestMatchers(HttpMethod.DELETE, "/account/{iban}", "/cards/{id}", "/credits/{contractNumber}", "deposits/{iban}")
                        .hasRole(Role.SUPER_USER.name())

                        .requestMatchers(HttpMethod.POST, "/cards", "/credits")
                        .hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.POST, "/account", "/deposits")
                        .access(new AuthorizeUserForAction(authorizationCheckers.get("checkCustomerForCreate")))

                        .requestMatchers(HttpMethod.PUT, "/account", "/deposits/{iban}")
                        .access(new AuthorizeUserForAction(authorizationCheckers.get("checkCustomerForUpdate")))

                        .requestMatchers(HttpMethod.PUT, "/cards/{id}", "/credits/{contractNumber}")
                        .hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/account", "/cards", "/credits", "/deposits",
                                "/account/customer/{id}", "/cards/client/{id}", "/credits/customers/{id}", "/deposits/filter")
                        .hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/account/{iban}", "/cards/{id}", "/credits/{contractNumber}", "/deposits/{iban}")
                        .access(new AuthorizeUserForAction(authorizationCheckers.get("checkCustomerForGet")))

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .addFilterBefore(cachingRequestBodyFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtCustomDecoder();
    }

}
