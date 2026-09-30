package ru.practicum.collector.service.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.sensor.MotionSensorEvent;
import ru.practicum.collector.dto.sensor.SensorEvent;
import ru.practicum.collector.dto.sensor.SensorTypeNames;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component(value = SensorTypeNames.MOTION_SENSOR_EVENT)
@SuppressWarnings("unused")
public class MotionSensorEventHandler extends SensorEventHandlerBase<MotionSensorAvro> {
    @Autowired
    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro getPayload(SensorEvent event) {
        MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setMotion(motionSensorEvent.isMotion())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();
    }
}
