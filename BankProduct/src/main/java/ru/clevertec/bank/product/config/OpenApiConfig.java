package ru.clevertec.bank.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Bank Product Service Application"),
        servers = @Server(url = "http://localhost:8081"))
public class OpenApiConfig {
}