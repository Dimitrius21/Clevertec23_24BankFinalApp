package ru.clevertec.bank.product.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс реализующий кэш для хранения сущностей по алгоритму LRU с синхронизацией.
 *
 * @param <V> тип кэшируемой сущности
 */
@Slf4j
public class CacheLruSync<V> implements Cacheable<String,V> {

    private String entityRepo;

    private Map<String, V> cache;

    public CacheLruSync(String entityRepo, int maxCapacity) {
        cache = new CacheLruBase<>(maxCapacity);
        this.entityRepo = entityRepo;
        log.info("Cache Lru has been created  for {}", entityRepo);
    }


    @Override
    public synchronized V put(String key, V value) {
        return cache.put(key, value);
    }

    @Override
    public synchronized V get(Object key) {
        return cache.get(key);
    }

    @Override
    public synchronized V remove(Object key) {
        return cache.remove(key);
    }

    @Override
    public void setRepositoriesName(String name) {
        entityRepo = name;
    }
}

