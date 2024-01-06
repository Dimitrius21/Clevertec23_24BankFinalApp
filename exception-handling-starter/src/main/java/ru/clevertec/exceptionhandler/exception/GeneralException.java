package ru.clevertec.exceptionhandler.exception;

public class GeneralException extends RuntimeException{

    private int code;

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralException(String message, int code) {
        super(message);
        this.code = code;
    }
}
