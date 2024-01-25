package ru.clevertec.bank.product.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParseRequest {

    public String getLastSubString(HttpServletRequest request) {
        String[] urlParts = request.getRequestURL().toString().split("/");
        return urlParts[urlParts.length - 1];
    }

    public String getEntityName(HttpServletRequest request) {
        String[] urlParts = request.getRequestURI().split("/");
        return urlParts[1];
    }

}
