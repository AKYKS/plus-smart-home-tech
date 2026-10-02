package ru.practicum.analyzer.service.snapshot.value.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@SuppressWarnings("unused")
public class SwitchHandler implements ValueHandler {
    private static final String SWITCH = "SWITCH";

    @Override
    public Class<?> getDataClass() {
        return SwitchSensorAvro.class;
    }

    @Override
    public int handle(Object data, String type) {
        SwitchSensorAvro switchSensor = (SwitchSensorAvro) data;
        if (type.equals(SWITCH)) {
            return switchSensor.getState() ? 1 : 0;
        }
        return 0;
    }
}
