package ru.clevertec.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.exceptionhandler.domain.ErrorIfo;
import ru.clevertec.exceptionhandler.domain.ValidationExceptionResponse;
import ru.clevertec.exceptionhandler.domain.Violation;
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorIfo> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Throwable rootCause = exception.getRootCause();
        ErrorIfo errorIfo = new ErrorIfo(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                Objects.nonNull(rootCause) ? rootCause.getMessage() : exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorIfo);
    }

    @ExceptionHandler(AccessDeniedForRoleException.class)
    public ResponseEntity<ErrorIfo> handleAccessDeniedForRoleException(AccessDeniedForRoleException exception) {
        ErrorIfo errorIfo = new ErrorIfo(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorIfo);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorIfo> handleInternalServerErrorException(InternalServerErrorException exception) {
        ErrorIfo errorIfo = new ErrorIfo(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorIfo);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationExceptionResponse> handleConstraintValidationException(ConstraintViolationException exception) {
        List<Violation> violations = exception.getConstraintViolations()
                .stream()
                .map(constraintViolation -> new Violation(constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ValidationExceptionResponse(violations));
    }

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<Violation> violations = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ValidationExceptionResponse(violations));
    }*/ //TODO Ambiguous @ExceptionHandler method mapped for [class org.springframework.web.bind.MethodArgumentNotValidException]

}
