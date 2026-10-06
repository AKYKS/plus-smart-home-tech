package ru.practicum.analyzer.service.snapshot.value.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
@SuppressWarnings("unused")
public class LightHandler implements ValueHandler {
    private static final String LUMINOSITY = "LUMINOSITY";

    @Override
    public Class<?> getDataClass() {
        return LightSensorAvro.class;
    }

    @Override
    public int handle(Object data, String type) {
        LightSensorAvro lightSensor = (LightSensorAvro) data;
        if (type.equals(LUMINOSITY)) {
            return lightSensor.getLuminosity();
        }
        return 0;
    }
}