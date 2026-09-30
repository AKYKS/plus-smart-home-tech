package ru.practicum.collector.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.collector.exception.HandlerNotFoundException;

@RestControllerAdvice
@SuppressWarnings("unused")
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        // Можно собрать список ошибок полей, если нужно. Сейчас — общее сообщение.
        return createErrorResponse(
                "Validation failed: check request fields",
                "validation_error",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex
    ) {
        return createErrorResponse(
                "Request body is invalid or malformed JSON",
                "bad_request_body",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HandlerNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlerNotFound(HandlerNotFoundException e) {
        return createErrorResponse(e.getMessage(), "handler not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
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
}
