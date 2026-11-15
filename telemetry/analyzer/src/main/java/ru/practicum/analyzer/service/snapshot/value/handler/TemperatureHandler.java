package ru.practicum.analyzer.service.snapshot.value.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
@SuppressWarnings("unused")
public class TemperatureHandler implements ValueHandler {
    private static final String TEMPERATURE = "TEMPERATURE";

    @Override
    public Class<?> getDataClass() {
        return TemperatureSensorAvro.class;
    }

    @Override
    public int handle(Object data, String type) {
        TemperatureSensorAvro temperatureSensor = (TemperatureSensorAvro) data;
        if (type.equals(TEMPERATURE)) {
            return temperatureSensor.getTemperatureC();
        }
        return 0;
    }
}
