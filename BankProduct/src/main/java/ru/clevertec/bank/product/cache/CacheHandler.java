package ru.clevertec.bank.product.cache;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Класс реализующий прокси для оборачивания вызова методов репозиториев для их кэширования
 */
public class CacheHandler implements InvocationHandler {

    private JpaRepository obj;
    private Cacheable cache;

    public CacheHandler(JpaRepository obj, Cacheable cache) {
        this.obj = obj;
        this.cache = cache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            switch (method.getName()) {
                case "findById":
                    return getByIdHandle(proxy, method, args);
                case "deleteById":
                    return deleteByIdHandle(proxy, method, args);
                case "save":
                    return createHandle(proxy, method, args);

            }
        } catch (NoSuchElementException ex) {
        }
        return method.invoke(obj, args);
    }

    private Object getByIdHandle(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object entity = cache.get(args[0]);
        if (entity != null) {
            return Optional.of(entity);
        } else {
            Optional<?> optionalEntity = (Optional<?>) method.invoke(obj, args);
            if (optionalEntity.isPresent()) {
                cache.put(args[0], optionalEntity.get());
            }
            return optionalEntity;
        }
    }

    private Object deleteByIdHandle(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object res = method.invoke(obj, args);
        cache.remove(args[0]);
        return res;
    }

    private Object createHandle(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object res = method.invoke(obj, args);
        Object id = getId(res);
        cache.put(id, res);
        return res;
    }

    private Object getId(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Field fieldId = Arrays.stream(fields).filter(f -> f.isAnnotationPresent(Id.class)).findFirst().get();
        fieldId.setAccessible(true);
        Object id = fieldId.get(obj);
        return id;
    }

}
