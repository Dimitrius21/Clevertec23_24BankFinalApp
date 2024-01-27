package ru.clevertec.bank.currency.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateTimeDeserializeTest {

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext ctxt;

    @Test
    void deserialize() throws IOException {
        DateTimeDeserialize deserialize = new DateTimeDeserialize();
        String date = "2024-01-15T14:00:10.604498616+03:00";
        ZoneId zone = ZoneId.of("GMT+3");
        ZonedDateTime expect = ZonedDateTime.of(2024, 1, 15, 14, 0, 10, 604498616, zone);

        when(parser.getText()).thenReturn(date);

        ZonedDateTime dateTime = deserialize.deserialize(parser, ctxt);

        Assertions.assertThat(dateTime).isEqualTo(expect);
    }
}