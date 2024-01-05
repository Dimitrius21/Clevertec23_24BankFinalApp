package ru.clevertec.exceptionhandler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.exceptionhandler.domain.ErrorIfo;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;


import java.time.LocalDateTime;

/**
 * Класс для обработки возникающих в приложении исключений
 */

@RestControllerAdvice
@ConditionalOnMissingBean
public class AppExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ResourceNotFountException.class)
    protected ResponseEntity<Object> handleEntityNotFoundEx(ResourceNotFountException ex, WebRequest request) {
        ErrorIfo error = new ErrorIfo(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex.getErrorDetails());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidRequestParametersException.class)
    protected ResponseEntity<Object> handleNotValidRequestData(NotValidRequestParametersException ex, WebRequest request) {
        ErrorIfo error = new ErrorIfo(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyIncorrectException.class)
    protected ResponseEntity<Object> handleNotValidRequestData(RequestBodyIncorrectException ex, WebRequest request) {
        ErrorIfo error = new ErrorIfo(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
