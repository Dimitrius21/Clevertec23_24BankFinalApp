package ru.clevertec.exceptionhandler.domain;

public record ErrorInfo(Integer errorCode,
                        String errorMessage) {
}