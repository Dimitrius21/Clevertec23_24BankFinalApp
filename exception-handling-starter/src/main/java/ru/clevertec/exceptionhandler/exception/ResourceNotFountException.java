package ru.clevertec.exceptionhandler.exception;

import lombok.Getter;

/**
 * Класс для генерации исключения в случае отсутствия ресурса(записи) в постоянном хранилище
 */
@Getter
public class ResourceNotFountException extends RuntimeException{

    private String errorDetails;

    public ResourceNotFountException(String message) {
        super(message);
    }

    public ResourceNotFountException() {
    }

    public ResourceNotFountException(String message, String errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }
}
