package ru.clevertec.bank.product.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Profile("prod")
@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperties rabbitProperties;

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(rabbitProperties.getExchange());
    }

    @Bean
    public Queue depositQueue() {
        return QueueBuilder.durable(rabbitProperties.getQueue().getDeposit())
                .build();
    }

    @Bean
    public Binding depositQueueBinding() {
        return BindingBuilder.bind(depositQueue())
                .to(headersExchange())
                .whereAny(Map.of(rabbitProperties.getHeader().getKey(), rabbitProperties.getHeader().getDeposit()))
                .match();
    }

}
