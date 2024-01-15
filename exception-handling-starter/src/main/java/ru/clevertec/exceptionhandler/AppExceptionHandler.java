package ru.clevertec.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;
import ru.clevertec.exceptionhandler.domain.ValidationExceptionResponse;
import ru.clevertec.exceptionhandler.domain.Violation;
import ru.clevertec.exceptionhandler.exception.AccessDeniedForRoleException;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;
import ru.clevertec.loggingstarter.annotation.Loggable;

import java.util.List;
import java.util.Objects;

@Loggable
@RestControllerAdvice
@ConditionalOnMissingBean
public class AppExceptionHandler {


    @ExceptionHandler(ResourceNotFountException.class)
    public ResponseEntity<ErrorInfo> handleEntityNotFoundEx(ResourceNotFountException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidRequestParametersException.class)
    public ResponseEntity<ErrorInfo> handleNotValidRequestData(NotValidRequestParametersException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyIncorrectException.class)
    public ResponseEntity<ErrorInfo> handleNotValidRequestData(RequestBodyIncorrectException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Throwable rootCause = exception.getRootCause();
        return sendResponse(Objects.nonNull(rootCause) ? rootCause.getMessage() : exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedForRoleException.class)
    public ResponseEntity<ErrorInfo> handleAccessDeniedForRoleException(AccessDeniedForRoleException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorInfo> handleInternalServerErrorException(InternalServerErrorException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorInfo> propertyReferenceException(PropertyReferenceException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<Violation> violations = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ValidationExceptionResponse(violations));
    }

    private ResponseEntity<ErrorInfo> sendResponse(String message, HttpStatus httpStatus) {
        ErrorInfo errorInfo = new ErrorInfo(httpStatus.value(), message);
        return ResponseEntity.status(httpStatus).body(errorInfo);
    }

}
