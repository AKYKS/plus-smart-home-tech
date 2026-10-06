package ru.practicum.collector.service.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
@SuppressWarnings("unused")
public class ClimateSensorEventHandler extends SensorEventHandlerBase<ClimateSensorAvro> {

    public ClimateSensorEventHandler(
            @Value("${kafka.topics.sensor-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    protected ClimateSensorAvro getPayload(SensorEventProto event) {
        ClimateSensorProto climateSensorEvent = event.getClimateSensor();
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .setHumidity(climateSensorEvent.getHumidity())
                .setCo2Level(climateSensorEvent.getCo2Level())
                .build();
    }
}
