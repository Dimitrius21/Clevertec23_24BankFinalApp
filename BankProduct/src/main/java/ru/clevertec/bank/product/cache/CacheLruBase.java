package ru.clevertec.bank.product.cache;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс реализующий кэш, для хранения сущностей по алгоритму LRU.
 *
 * @param <V> тип кэшируемой сущности
 */
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class CacheLruBase<V> extends LinkedHashMap<String, V> {
    private final int maxCapacity;

    public CacheLruBase(int maxCapacity) {
        super(maxCapacity, 0.75f, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxCapacity;
    }
}

