package ru.clevertec.exceptionhandler.domain;

import java.util.List;

public record ValidationExceptionResponse(List<Violation> violations) {
}
