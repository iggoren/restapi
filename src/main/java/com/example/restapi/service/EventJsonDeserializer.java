package com.example.restapi.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class EventJsonDeserializer extends JsonObjectDeserializer<Event> {
    //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");- LocalDate format
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    @Override
    protected Event deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec, JsonNode treeNode) {
        Event event = new Event();
        event.setSummary(treeNode.get("summary").asText());
        event.setDescription(treeNode.get("description").asText());

        // Десериализуем даты и время
        JsonNode startNode = treeNode.get("start");

        if (startNode != null) {
            EventDateTime startDateTime = new EventDateTime();
            EventDateTime endDateTime = new EventDateTime();
            LocalDateTime startLocalDateTime = LocalDateTime.parse(startNode.get("dateTime").asText(), formatter);
            LocalDateTime endLocalDateTime = startLocalDateTime.plusMinutes(30);
            Instant startInstant = startLocalDateTime.toInstant(ZoneOffset.UTC);
            Instant endInstant = endLocalDateTime.toInstant(ZoneOffset.UTC);

            startDateTime.setDateTime(new DateTime(startInstant.toEpochMilli()));
            endDateTime.setDateTime(new DateTime(endInstant.toEpochMilli()));

            event.setStart(startDateTime);
            event.setEnd(endDateTime);
        }
        return event;
    }
}

