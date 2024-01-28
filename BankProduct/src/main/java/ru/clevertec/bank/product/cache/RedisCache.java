package ru.clevertec.bank.product.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Класс реализующий кэш, для хранения сущностей в Redis
 */
@Slf4j
public class RedisCache implements Cacheable<String, Object> {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String entityRepo;

    public RedisCache(RedisTemplate<String, Object> redisTemplate, String entityRepo) {
        this.redisTemplate = redisTemplate;
        this.entityRepo = entityRepo;
        log.info("Cache with Redis has been created  for {}", entityRepo);
    }

    @Override
    public Object put(String key, Object value) {
        redisTemplate.opsForHash().put(entityRepo, key, value);
        return value;
    }

    @Override
    public Object get(Object key) {
        Object value = redisTemplate.opsForHash().get(entityRepo, key);
        return value;
    }

    @Override
    public Object remove(Object key) {
        return redisTemplate.opsForHash().delete(entityRepo, key);
    }

}
