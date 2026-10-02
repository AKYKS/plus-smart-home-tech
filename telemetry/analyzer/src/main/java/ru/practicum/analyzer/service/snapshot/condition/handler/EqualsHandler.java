package ru.practicum.analyzer.service.snapshot.condition.handler;

import org.springframework.stereotype.Component;

@Component("EQUALS")
@SuppressWarnings("unused")
public class EqualsHandler implements ConditionHandler{
    @Override
    public boolean handle(int sensorValue, int conditionValue) {
        return sensorValue == conditionValue;
    }
}
