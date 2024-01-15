package ru.clevertec.bank.product.cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CacheLfuTest {

    private CacheLfuSync<String> cache;

    @BeforeEach
    public void initCache() {
        cache = new CacheLfuSync<>("Test", 2);
    }

    @Test
    void putTest() {
        String key = "1";
        cache.put(key, "1");
        String res = cache.get(key);
        Assertions.assertThat(res).isEqualTo("1");
    }

    @Test
    void getTest() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.get("1");
        cache.put("3", "3");
        String res = cache.get("1");
        Assertions.assertThat(res).isEqualTo("1");
    }

    @Test
    void getAbsentValueTest() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.get("1");
        cache.put("3", "3");
        String res = cache.get("2");
        Assertions.assertThat(res).isNull();
    }

    @Test
    void delete() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.get("1");
        cache.remove("1");
        String res = cache.get("1");
        Assertions.assertThat(res).isNull();
    }
}