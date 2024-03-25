package com.internship.socialnetwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";

    private static final String STATUS = "status";

    private static final String ERROR = "error";

    private static final String MESSAGE = "message";

    private static final String PATH = "path";

    private static final String BAD_REQUEST_MESSAGE = "Bad Request";

    private static final String NOT_FOUND_MESSAGE = "Not Found";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIMESTAMP, LocalDateTime.now());
        errorResponse.put(STATUS, HttpStatus.BAD_REQUEST.value());
        errorResponse.put(ERROR, BAD_REQUEST_MESSAGE);
        errorResponse.put(MESSAGE, ex.getMessage());
        errorResponse.put(PATH, request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIMESTAMP, LocalDateTime.now());
        errorResponse.put(STATUS, HttpStatus.NOT_FOUND.value());
        errorResponse.put(ERROR, NOT_FOUND_MESSAGE);
        errorResponse.put(MESSAGE, ex.getMessage());
        errorResponse.put(PATH, request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
