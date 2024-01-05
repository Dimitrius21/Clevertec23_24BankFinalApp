package ru.clevertec.exceptionhandler.exception;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.MessageSourceAware;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

public class ExceptionHandlerAddingBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> parent = bean.getClass().getSuperclass();
        if (parent.getSimpleName().equals(ResponseEntityExceptionHandler.class)) {
            System.out.println(bean.getClass().getName() + " extends ResponseEntityExceptionHandler");
        }

/*        List<Class<?>> interfaces = Arrays.asList(bean.getClass().getInterfaces());
        if (interfaces.contains(MessageSourceAware.class)) {
            System.out.println(bean.getClass().getName() + "implements MessageSourceAware.class");
        }*/

        if ( bean.getClass().isAnnotationPresent(ControllerAdvice.class)) {
            System.out.println(bean.getClass().getName() + " contains RestControllerAdvice.class");

        }

        return bean;
    }
}
