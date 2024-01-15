package ru.clevertec.bank.customer.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConditionalOnBean(RabbitConfig.class)
@ConfigurationProperties(prefix = "rabbit")
public class RabbitProperties {

    private String exchange;
    private QueueProperties queue;

    @Data
    public static class QueueProperties {
        private String customer;
    }

}
