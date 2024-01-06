package ru.cleverte.controllerlogging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.cleverte.controllerlogging.aspect.ControllerLoggingAspect;

@Configuration
@EnableAspectJAutoProxy
//@EnableConfigurationProperties(StarterProperties.class)
public class ControllerLoggingStarterConfig {

    @Bean
    public ControllerLoggingAspect controllerLoggingAspect(){
     return new ControllerLoggingAspect();
    }
}
