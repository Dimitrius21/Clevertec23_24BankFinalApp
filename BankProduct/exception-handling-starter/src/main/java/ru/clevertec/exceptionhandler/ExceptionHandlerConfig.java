package ru.clevertec.exceptionhandler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(StarterProperties.class)
@ConditionalOnProperty(prefix = "exception.custom-handler", name = "enable", havingValue = "true")
public class ExceptionHandlerConfig {

    @Bean
    AppExceptionHandler exceptionHandlingAddBeanPostProcessor(){
        return new AppExceptionHandler();
    }
}
