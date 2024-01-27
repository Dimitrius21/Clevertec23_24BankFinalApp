package ru.clevertec.bank.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.clevertec.bank.product.cache.CacheLfuSync;
import ru.clevertec.bank.product.cache.CacheLruSync;
import ru.clevertec.bank.product.cache.Cacheable;
import ru.clevertec.bank.product.cache.RedisCache;

import java.util.function.Function;

/**
 * Класс для конфигурации объектов обеспечивающих кэширование данных при доступе в базу данных
 */
@Configuration
public class CacheConfig {

    @Value("${caching.size:10}")
    private int maxSize;

    @Bean
    @ConditionalOnProperty(prefix = "caching", name = "type", havingValue = "redis")
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "caching", name = "type", havingValue = "redis")
    public Function<String, Cacheable> CacheRedisBeanFactory(RedisTemplate redisTemplate) {
        return name -> CacheRedis(redisTemplate, name);
    }

    @Bean
    @ConditionalOnProperty(prefix = "caching", name = "type", havingValue = "lfu")
    public Function<String, Cacheable> CacheLfuBeanFactory() {
        return this::CacheLfu;
    }

    @Bean
    @ConditionalOnProperty(prefix = "caching", name = "type", havingValue = "lru")
    public Function<String, Cacheable> CacheLruBeanFactory() {
        return this::CacheLru;
    }


    @Bean
    @Scope(value = "prototype")
    public Cacheable CacheLfu(String name) {
        return new CacheLfuSync(name, maxSize);
    }

    @Bean
    @Scope(value = "prototype")
    public Cacheable CacheLru(String name) {
        return new CacheLruSync(name, maxSize);
    }

    @Bean
    @Scope(value = "prototype")
    public Cacheable CacheRedis(RedisTemplate redisTemplate, String entityRepo) {
        return new RedisCache(redisTemplate, entityRepo);
    }
}