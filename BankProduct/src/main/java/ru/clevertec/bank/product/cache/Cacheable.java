package ru.clevertec.bank.product.cache;

/**
 * Интерфейс определяющий операции для кэшируемой сущности
 *
 * @param <K> - тип ID
 * @param <V> - тип сущности
 */
public interface Cacheable<K, V> {

    V put(K key, V value);

    V get(Object key);

    V remove(Object key);

}
