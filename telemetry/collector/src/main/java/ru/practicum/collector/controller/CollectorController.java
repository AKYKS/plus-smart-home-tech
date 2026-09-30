package ru.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.sensor.SensorEvent;
import ru.practicum.collector.exception.HandlerNotFoundException;
import ru.practicum.collector.service.hub.HubEventHandler;
import ru.practicum.collector.service.sensor.SensorEventHandler;

import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CollectorController {
    private final Map<String, SensorEventHandler> sensorEventHandlers;
    private final Map<String, HubEventHandler> hubEventHandlers;

    @PostMapping("/sensors")
    public ResponseEntity<Void> collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        String handlerName = event.getType().name();
        SensorEventHandler handler = sensorEventHandlers.get(handlerName);
        if (handler == null) {
            throw new HandlerNotFoundException(handlerName);
        }
        handler.handle(event);
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/hubs")
    public ResponseEntity<Void> collectHubEvent(@Valid @RequestBody HubEvent event) {
        String handlerName = event.getType().name();
        HubEventHandler handler = hubEventHandlers.get(handlerName);
        if (handler == null) {
            throw new HandlerNotFoundException(handlerName);
        }
        handler.handle(event);
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }
}
