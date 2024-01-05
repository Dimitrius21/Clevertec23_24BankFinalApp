package ru.clevertec.bank.product.cache;

/**
 * Интерфейс определяющий операции для кэшируемой сущности
 * @param <K> - тип ID
 * @param <V> - тип сущности
 */
public interface Cacheable<K,V> {
    public V put(K key, V value);
    public V get(Object key);
    public V remove(Object key);

    public void setRepositoriesName(String name);
}
