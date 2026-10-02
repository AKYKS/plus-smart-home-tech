package ru.practicum.collector.dto.hub.device;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceRemovedHubEvent extends HubEvent {
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}