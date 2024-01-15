package ru.clevertec.loggingstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Slf4j
@Aspect
public class LoggingAspect {

    @Around("within(@ru.clevertec.loggingstarter.annotation.Loggable *)")
    public Object loggingMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(joinPoint.getArgs());
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String logMessage = """
                %s%s.%s :
                Request : %s
                Response : %s
                """.formatted("\n",
                className,
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                result);
        if (className.endsWith("Handler")) {
            log.error(logMessage);
        } else {
            log.info(logMessage);
        }
        return result;
    }

}
