package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
@SuppressWarnings("unused")
public class DeviceRemovedHubEventHandler extends HubEventHandlerBase<DeviceRemovedEventAvro> {

    public DeviceRemovedHubEventHandler(
            @Value("${kafka.topics.hub-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro getPayload(HubEventProto event) {
        DeviceRemovedEventProto deviceRemovedHubEvent = event.getDeviceRemoved();
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedHubEvent.getId())
                .build();
    }
}
