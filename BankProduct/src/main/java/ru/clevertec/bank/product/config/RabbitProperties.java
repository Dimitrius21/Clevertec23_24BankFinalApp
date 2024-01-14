package ru.clevertec.bank.product.config;

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
    private HeaderProperties header;

    @Data
    public static class QueueProperties {
        private String deposit;
        private String credit;
    }

    @Data
    public static class HeaderProperties {
        private String key;
        private String deposit;
        private String credit;
    }

}
