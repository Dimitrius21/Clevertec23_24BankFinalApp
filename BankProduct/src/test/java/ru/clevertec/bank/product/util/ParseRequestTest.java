package ru.clevertec.bank.product.util;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParseRequestTest {

    @Mock
    private HttpServletRequest request;



    @Test
    void getLastSubString() {
        when(request.getRequestURL()).thenReturn(new StringBuffer("www.localhost:8080/account/AABBCC"));

        String res = ParseRequest.getLastSubString(request);

        Assertions.assertThat(res).isEqualTo("AABBCC");
    }

    @Test
    void getEntityName() {
        when(request.getRequestURI()).thenReturn("/account/AABBCC");

        String res = ParseRequest.getEntityName(request);

        Assertions.assertThat(res).isEqualTo("account");
    }
}