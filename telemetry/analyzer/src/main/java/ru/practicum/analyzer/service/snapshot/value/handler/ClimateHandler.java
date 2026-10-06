package ru.practicum.analyzer.service.snapshot.value.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
@SuppressWarnings("unused")
public class ClimateHandler implements ValueHandler {
    private static final String TEMPERATURE = "TEMPERATURE";
    private static final String HUMIDITY = "HUMIDITY";
    private static final String CO2LEVEL = "CO2LEVEL";

    @Override
    public Class<?> getDataClass() {
        return ClimateSensorAvro.class;
    }

    @Override
    public int handle(Object data, String type) {
        ClimateSensorAvro climateSensor = (ClimateSensorAvro) data;
        return switch (type) {
            case TEMPERATURE -> climateSensor.getTemperatureC();
            case HUMIDITY -> climateSensor.getHumidity();
            case CO2LEVEL -> climateSensor.getCo2Level();
            default -> 0;
        };
    }
}
