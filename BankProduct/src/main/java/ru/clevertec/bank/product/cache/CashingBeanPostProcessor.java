package ru.clevertec.bank.product.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Класс реализующий Spring BeanPostProcessor для создания прокси, реализующего кэширование данных при обращении к сущностям в БД
 */
@Slf4j
@Component
public class CashingBeanPostProcessor implements BeanPostProcessor {

    @Value("${caching.type:}")
    private String cacheType;

    @Value("${caching.repository:}")
    private List<String> cachedRepositories;

    @Autowired
    private Function<String, Cacheable> cacheBeanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (cachedRepositories.size() > 0 && cachedRepositories.contains(beanName)) {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            boolean hasAppRepositoryInterface = Arrays.stream(interfaces).anyMatch(
                    i -> {
                        String interfaceName = i.getSimpleName();
                        return cachedRepositories.stream().anyMatch(s -> s.equalsIgnoreCase(interfaceName));
                    }
            );
            if (hasAppRepositoryInterface) {
                Cacheable cacheable = cacheBeanFactory.apply(beanName);
                CacheHandler handler = new CacheHandler((JpaRepository) bean, cacheable);
                Object proxyRepo = Proxy.newProxyInstance(bean.getClass().getClassLoader(), interfaces, handler);
                log.debug("Class {} has AppRepositoryInterface, Cache type - {}", bean.getClass().getName(), cacheable.getClass().getSimpleName());
                return proxyRepo;
            }
        }
        return bean;
    }
}
