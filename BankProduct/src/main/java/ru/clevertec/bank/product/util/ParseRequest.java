package ru.clevertec.bank.product.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

/**
 * Класс содержащий вспомогательные методы для получения данных из http запроса
 */
public class ParseRequest {

    /**
     * Метод возвращает id из url полученного запроса
     * @param request объект запроса
     * @return - id
     */
    public static long getId(HttpServletRequest request) {
        String[] urlParts = request.getRequestURL().toString().split("/");
        String stringId = urlParts[urlParts.length - 1];
        long id = Long.parseLong(stringId);
        return id;
    }

    public static String getLastSubString(HttpServletRequest request) {
        String[] urlParts = request.getRequestURL().toString().split("/");
        return urlParts[urlParts.length - 1];
    }

    public static String getEntityName(HttpServletRequest request) {
        String[] urlParts = request.getPathInfo().split("/");
        return urlParts[1];
    }
}
