package ru.practicum.analyzer.service.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    Class<?> getPayloadClass();

    void handle(HubEventAvro event);
}
