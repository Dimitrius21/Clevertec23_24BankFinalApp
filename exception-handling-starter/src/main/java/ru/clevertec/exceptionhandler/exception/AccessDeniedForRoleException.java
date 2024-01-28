package ru.clevertec.exceptionhandler.exception;

public class AccessDeniedForRoleException extends RuntimeException {

    public AccessDeniedForRoleException(String message) {
        super(message);
    }

}
