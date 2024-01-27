package ru.clevertec.exceptionhandler.exception;

public class NotValidRequestParametersException extends RuntimeException {

    public NotValidRequestParametersException(String message) {
        super(message);
    }

}
