package ru.practicum.analyzer.service.snapshot.value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.service.snapshot.value.handler.ValueHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ValueProcessor {
    private final Map<Class<?>, ValueHandler> valueHandlerMap;

    @Autowired
    public ValueProcessor(Set<ValueHandler> valueHandlers) {
        this.valueHandlerMap = valueHandlers.stream()
                .collect(Collectors.toMap(
                        ValueHandler::getDataClass,
                        Function.identity()
                ));
    }

    public int process(Object data, String type) {
        ValueHandler handler = valueHandlerMap.get(data.getClass());
        if (handler == null) {
            return 0;
        }
        return handler.handle(data, type);
    }
}
