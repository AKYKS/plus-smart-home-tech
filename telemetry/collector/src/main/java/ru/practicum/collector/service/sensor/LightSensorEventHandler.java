package ru.practicum.collector.service.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.sensor.LightSensorEvent;
import ru.practicum.collector.dto.sensor.SensorEvent;
import ru.practicum.collector.dto.sensor.SensorTypeNames;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component(value = SensorTypeNames.LIGHT_SENSOR_EVENT)
@SuppressWarnings("unused")
public class LightSensorEventHandler extends SensorEventHandlerBase<LightSensorAvro> {

    public LightSensorEventHandler(
            @Value("${kafka.topics.sensor-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    protected LightSensorAvro getPayload(SensorEvent event) {
        LightSensorEvent lightSensorEvent = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }
}
