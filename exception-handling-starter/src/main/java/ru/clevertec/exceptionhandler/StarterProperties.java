package ru.clevertec.exceptionhandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "exception.custom-handler")
public class StarterProperties {

    private boolean enable;

}
