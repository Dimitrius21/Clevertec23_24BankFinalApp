package ru.clevertec.loggingstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("within(@ru.clevertec.loggingstarter.annotation.Loggable *)")
    public void isClassWithLoggableAnnotation() {
    }

    @Before("isClassWithLoggableAnnotation()")
    public void loggingBeforeController(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        log.info("invoked method {} with args {}",
                signature.getDeclaringType() + "." + signature.getName(),
                Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", ")));
    }

    @AfterReturning(pointcut = "isClassWithLoggableAnnotation()", returning = "result")
    public void loggingAfterController(Object result) {
        log.info("method return: {}", result);
    }

    @AfterReturning(pointcut = "within(@ru.clevertec.loggingstarter.annotation.ExceptionLoggable *)", returning = "result")
    public void loggingAfterHandler(Object result) {
        log.error("method return: {}", result);
    }

}
