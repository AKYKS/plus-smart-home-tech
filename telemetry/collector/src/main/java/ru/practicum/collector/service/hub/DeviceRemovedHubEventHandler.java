package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubTypeNames;
import ru.practicum.collector.dto.hub.device.DeviceRemovedHubEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component(value = HubTypeNames.DEVICE_REMOVED_EVENT)
@SuppressWarnings("unused")
public class DeviceRemovedHubEventHandler extends HubEventHandlerBase<DeviceRemovedEventAvro> {

    public DeviceRemovedHubEventHandler(
            @Value("${kafka.topics.hub-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    protected DeviceRemovedEventAvro getPayload(HubEvent event) {
        DeviceRemovedHubEvent deviceRemovedHubEvent = (DeviceRemovedHubEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedHubEvent.getId())
                .build();
    }
}
