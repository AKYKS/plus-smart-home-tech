package ru.practicum.collector.dto.hub.device.added;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedHubEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
