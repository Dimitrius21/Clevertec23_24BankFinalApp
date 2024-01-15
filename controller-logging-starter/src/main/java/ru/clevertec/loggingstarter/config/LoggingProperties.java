package ru.clevertec.loggingstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aop.logging")
public class LoggingProperties {

    private boolean enabled;

}
