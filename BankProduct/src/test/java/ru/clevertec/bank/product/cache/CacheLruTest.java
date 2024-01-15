package ru.clevertec.bank.product.cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CacheLruTest {

    private CacheLruSync<String> cache;

    @BeforeEach
    public void initCache() {
        cache = new CacheLruSync<>("Test", 2);
    }

    @Test
    void putTest() {
        cache.put("1", "10");
        String res = cache.get("1");
        Assertions.assertThat(res).isEqualTo("10");
    }

    @Test
    void getTest() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");

        String res = cache.get("3");
        Assertions.assertThat(res).isEqualTo("3");
    }

    @Test
    void valueIsAbsentTest() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.get("1");
        cache.get("1");
        cache.get("2");
        cache.put("3", "3");

        String res = cache.get("1");
        Assertions.assertThat(res).isNull();
    }

    @Test
    void deleteTest() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.remove("1");
        String res = cache.get("1");
        Assertions.assertThat(res).isNull();
    }
}