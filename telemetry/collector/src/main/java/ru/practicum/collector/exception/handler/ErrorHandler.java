package ru.practicum.collector.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.collector.exception.HandlerNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@SuppressWarnings("unused")
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        List<FieldErrorInfo> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorInfo(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return createErrorResponse(
                "Validation failed: check request fields",
                "validation_error",
                HttpStatus.BAD_REQUEST,
                fieldErrors
        );
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex
    ) {
        return createErrorResponse(
                "Request body is invalid or malformed JSON",
                "bad_request_body",
                HttpStatus.BAD_REQUEST,
                null
        );
    }

    @ExceptionHandler(HandlerNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlerNotFound(HandlerNotFoundException e) {
        return createErrorResponse(e.getMessage(), "handler not found", HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> unexpected(RuntimeException e) {
        return createErrorResponse(
                e.getMessage(),
                "unexpected error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }

    private ResponseEntity<ErrorMessage> createErrorResponse(String message, String reason,
                                                             HttpStatus status, List<FieldErrorInfo> errors) {
        return ResponseEntity.status(status).body(
                new ErrorMessage(
                        message,
                        reason,
                        errors
                )
        );
    }
}
