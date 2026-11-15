package ru.practicum.analyzer.service.snapshot.condition.handler;

import org.springframework.stereotype.Component;

@Component("LOWER_THAN")
@SuppressWarnings("unused")
public class LowerThanHandler implements ConditionHandler{
    @Override
    public boolean handle(int sensorValue, int conditionValue) {
        return sensorValue < conditionValue;
    }
}
