package ru.practicum.collector.exception;

import lombok.Getter;

@Getter
public class HandlerNotFoundException extends RuntimeException {
    private final String name;

    public HandlerNotFoundException(String name) {
        super(String.format("Handler '%s' was not found", name));
        this.name = name;
    }
}
