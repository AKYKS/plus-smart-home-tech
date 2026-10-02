package ru.practicum.analyzer.service.snapshot.value.handler;

public interface ValueHandler {
    Class<?> getDataClass();

    int handle(Object data, String type);
}
