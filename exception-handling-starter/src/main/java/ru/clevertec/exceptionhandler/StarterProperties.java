package ru.clevertec.exceptionhandler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "exception.custom-handler")
public class StarterProperties {

    private boolean enable;

}
