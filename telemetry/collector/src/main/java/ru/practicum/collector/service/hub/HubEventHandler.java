package ru.practicum.collector.service.hub;

import ru.practicum.collector.dto.hub.HubEvent;

public interface HubEventHandler {
    void handle(HubEvent event);
}
