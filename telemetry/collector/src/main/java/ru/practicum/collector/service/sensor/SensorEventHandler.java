package ru.practicum.collector.service.sensor;

import ru.practicum.collector.dto.sensor.SensorEvent;

public interface SensorEventHandler {
    void handle(SensorEvent event);
}
