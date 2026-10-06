package ru.practicum.collector.service.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@SuppressWarnings("unused")
public class SwitchSensorEventHandler extends SensorEventHandlerBase<SwitchSensorAvro> {

    public SwitchSensorEventHandler(
            @Value("${kafka.topics.sensor-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }


    @Override
    protected SwitchSensorAvro getPayload(SensorEventProto event) {
        SwitchSensorProto switchSensorEvent = event.getSwitchSensor();
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }
}