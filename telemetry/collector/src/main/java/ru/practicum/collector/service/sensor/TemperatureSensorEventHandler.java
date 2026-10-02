package ru.practicum.collector.service.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.sensor.SensorEvent;
import ru.practicum.collector.dto.sensor.SensorTypeNames;
import ru.practicum.collector.dto.sensor.TemperatureSensorEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component(value = SensorTypeNames.TEMPERATURE_SENSOR_EVENT)
@SuppressWarnings("unused")
public class TemperatureSensorEventHandler extends SensorEventHandlerBase<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(
            @Value("${kafka.topics.sensor-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    protected TemperatureSensorAvro getPayload(SensorEvent event) {
        TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();
    }
}
