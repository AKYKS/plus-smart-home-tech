package ru.practicum.analyzer.service.snapshot.condition.handler;

public interface ConditionHandler {
    boolean handle(int sensorValue, int conditionValue);
}
