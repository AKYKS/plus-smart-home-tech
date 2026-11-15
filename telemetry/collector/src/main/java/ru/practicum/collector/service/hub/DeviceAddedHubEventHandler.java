package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
@SuppressWarnings("unused")
public class DeviceAddedHubEventHandler extends HubEventHandlerBase<DeviceAddedEventAvro> {

    public DeviceAddedHubEventHandler(
            @Value("${kafka.topics.hub-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro getPayload(HubEventProto event) {
        DeviceAddedEventProto deviceAddedHubEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedHubEvent.getId())
                .setType(mapDeviceType(deviceAddedHubEvent.getType()))
                .build();
    }

    private DeviceTypeAvro mapDeviceType(DeviceTypeProto deviceTypeProto) {
        return switch (deviceTypeProto) {
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            default -> null;
        };
    }
}
