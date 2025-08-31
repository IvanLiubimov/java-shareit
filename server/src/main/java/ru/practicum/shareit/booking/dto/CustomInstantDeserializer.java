package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CustomInstantDeserializer extends StdDeserializer<Instant> {
    public CustomInstantDeserializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}