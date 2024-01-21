package ru.clevertec.bank.product.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.clevertec.bank.product.filter.ExceptionFilter;
import ru.clevertec.bank.product.secure.AuthorizeUserForAction;
import ru.clevertec.bank.product.secure.CheckAccountByRequestedIban;
import ru.clevertec.bank.product.secure.CheckUserInRequest;
import ru.clevertec.bank.product.secure.JwtCustomDecoder;
import ru.clevertec.bank.product.util.Role;

import java.util.Map;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Autowired
    private CheckAccountByRequestedIban checkAccountByRequestedIban;

    @Autowired
    Map<String, CheckUserInRequest> authorizationCheckers ;

/*    @Autowired
    private CheckUserByRequestId checkUserByRequestId;
    */

    @Autowired
    private final ExceptionFilter exceptionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        .requestMatchers(HttpMethod.DELETE, "/*").hasRole(Role.SUPER_USER.name())
                        .requestMatchers(HttpMethod.POST, "/*").hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.PUT, "/*")
                        .access(new AuthorizeUserForAction(authorizationCheckers.get("checkCustomerForUpdate")))
                        /*.requestMatchers(HttpMethod.PUT, "/credits/{contractNumber}").access(new AuthorizeUserForAction())
                        .requestMatchers(HttpMethod.PUT, "/account").access(new AuthorizeUserForAction())
                        .requestMatchers(HttpMethod.PUT, "/deposits/{iban}").access(new AuthorizeUserForAction())*/
                        .requestMatchers(HttpMethod.GET, "/account", "/cards", "/credits", "/deposits").hasRole(Role.ADMINISTRATOR.name())

                        .requestMatchers(HttpMethod.GET, "/account/{iban}", "/cards/{id}", "/credits/{contractNumber}", "/deposits/{iban}" )
                             .access(new AuthorizeUserForAction(authorizationCheckers.get("checkCustomerForGetEntity")))

/*                        .requestMatchers(HttpMethod.GET, "/account/customer/{id}").access(new AuthorizeUserForAction())
                        .requestMatchers(HttpMethod.GET, "/cards/client/{id}").access(new AuthorizeUserForAction())
                        .requestMatchers(HttpMethod.GET, "/credits/customers/{id}").access(new AuthorizeUserForAction())*/

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) ->
                        exceptionFilter.handleException(response, authException)))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter())))
/*                .exceptionHandling(handler -> handler.authenticationEntryPoint(new ForbiddenEntryPoint()))
                .addFilterBefore(exceptionFilter, JwtFilter.class)*/
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
