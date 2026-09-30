package ru.practicum.collector.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private String reason;
}
