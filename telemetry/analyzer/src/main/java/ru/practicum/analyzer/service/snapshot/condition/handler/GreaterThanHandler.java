package ru.practicum.analyzer.service.snapshot.condition.handler;

import org.springframework.stereotype.Component;

@Component("GREATER_THAN")
@SuppressWarnings("unused")
public class GreaterThanHandler implements ConditionHandler{
    @Override
    public boolean handle(int sensorValue, int conditionValue) {
        return sensorValue > conditionValue;
    }
}