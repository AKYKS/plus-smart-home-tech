package ru.practicum.collector.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.collector.exception.HandlerNotFoundException;

@RestControllerAdvice
@SuppressWarnings("unused")
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorMessage> unexpected(RuntimeException e) {
        return createErrorResponse(
                e.getMessage(),
                "unexpected error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorMessage> createErrorResponse(String message, String reason, HttpStatus status) {
        return ResponseEntity.status(status).body(
                new ErrorMessage(
                        message,
                        reason
                )
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handlerNotFound(HandlerNotFoundException e) {
        return createErrorResponse(e.getMessage(), "handler not found", HttpStatus.BAD_REQUEST);
    }
}
