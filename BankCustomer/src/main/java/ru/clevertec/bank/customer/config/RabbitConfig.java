package ru.clevertec.bank.customer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperties rabbitProperties;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitProperties.getExchange());
    }

    @Bean
    public Queue customerQueue() {
        return QueueBuilder.durable(rabbitProperties.getQueue().getCustomer())
                .build();
    }

    @Bean
    public Binding customerQueueBinding() {
        return BindingBuilder.bind(customerQueue())
                .to(directExchange())
                .with(rabbitProperties.getQueue().getCustomer());
    }

}
