package ru.cleverte.controllerlogging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Класс реализующий  Around Advice для логирования запросов для слоя Controller
 */
@Slf4j
@Aspect
public class ControllerLoggingAspect {

    @Around("within(ru.clevertec.*.*.controller..*)")
    public Object controllersMethodsLogging(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        log.info("invoked method {} with args {}",
                signature.getDeclaringType() + "." + signature.getName(),
                Arrays.stream(pjp.getArgs()).map(Object::toString).collect(Collectors.joining(", ")));
        Object result = pjp.proceed();
        log.info("method return: {}", result);
        return result;
    }
}
