package ru.clevertec.exceptionhandler.exception;

import lombok.Getter;

/**
 * Класс для генерации исключения в случае не валидных данных в параметрах запроса
 */
@Getter
public class NotValidRequestParametersException extends RuntimeException{
    private long errorCode;
    public NotValidRequestParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidRequestParametersException(String message, long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotValidRequestParametersException(String message) {
        super(message);
    }
}
