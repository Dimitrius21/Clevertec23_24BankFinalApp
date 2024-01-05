package ru.clevertec.exceptionhandler.exception;

import lombok.Getter;

/**
 * Класс для генерации исключения в случае когда полученные в теле запроса данные не корректны
 */

@Getter
public class RequestBodyIncorrectException extends RuntimeException{
    private String errorDetails;

    public RequestBodyIncorrectException(String message, String errorCode) {
        super(message);
        this.errorDetails = errorCode;
    }

    public RequestBodyIncorrectException(String message) {
        super(message);
    }
}


