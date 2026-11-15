package ru.practicum.analyzer.service.snapshot.value.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
@SuppressWarnings("unused")
public class MotionHandler implements ValueHandler {
    private static final String MOTION = "MOTION";

    @Override
    public Class<?> getDataClass() {
        return MotionSensorAvro.class;
    }

    @Override
    public int handle(Object data, String type) {
        MotionSensorAvro motionSensor = (MotionSensorAvro) data;
        if (type.equals(MOTION)) {
            return motionSensor.getMotion() ? 1 : 0;
        }
        return 0;
    }
}
