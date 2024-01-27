package ru.clevertec.bank.product.cache;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Класс реализующий прокси для оборачивания вызова методов репозиториев для их кэширования
 */
public class CacheHandler implements InvocationHandler {

    private final JpaRepository<Object, Object> obj;
    private final Cacheable<Object, Object> cache;

    public CacheHandler(JpaRepository<Object, Object> obj, Cacheable<Object, Object> cache) {
        this.obj = obj;
        this.cache = cache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return switch (method.getName()) {
            case "findById" -> getByIdHandle(method, args);
            case "deleteById" -> deleteByIdHandle(method, args);
            case "save" -> createHandle(method, args);
            default -> method.invoke(obj, args);
        };
    }

    private Object getByIdHandle(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object entity = cache.get(args[0]);
        if (entity != null) {
            return Optional.of(entity);
        } else {
            Optional<?> optionalEntity = (Optional<?>) method.invoke(obj, args);
            optionalEntity.ifPresent(v -> cache.put(args[0], v));
            return optionalEntity;
        }

    }

    private Object deleteByIdHandle(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object res = method.invoke(obj, args);
        cache.remove(args[0]);
        return res;
    }

    private Object createHandle(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object res = method.invoke(obj, args);
        Object id = getId(res);
        cache.put(id, res);
        return res;
    }

    private Object getId(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Field fieldId = Arrays.stream(fields).filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
        fieldId.setAccessible(true);
        Object id = fieldId.get(obj);
        return id;
    }

}
