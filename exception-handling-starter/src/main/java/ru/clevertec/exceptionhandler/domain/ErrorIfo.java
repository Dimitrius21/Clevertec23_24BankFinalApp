package ru.clevertec.exceptionhandler.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс представляющий тело ответа в случае ошибки при обработке запроса
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorIfo {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private int errorCode;
    private String errorMessage;
    private String errorDetails;

}