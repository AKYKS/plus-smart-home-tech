package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubTypeNames;
import ru.practicum.collector.dto.hub.device.DeviceRemovedHubEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component(value = HubTypeNames.DEVICE_REMOVED_EVENT)
@SuppressWarnings("unused")
public class DeviceRemovedHubEventHandler extends HubEventHandlerBase<DeviceRemovedEventAvro> {
    @Autowired
    public DeviceRemovedHubEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceRemovedEventAvro getPayload(HubEvent event) {
        DeviceRemovedHubEvent deviceRemovedHubEvent = (DeviceRemovedHubEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedHubEvent.getId())
                .build();
    }
}
