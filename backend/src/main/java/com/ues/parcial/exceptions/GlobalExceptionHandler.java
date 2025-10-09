package com.ues.parcial.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

// Global handler for all exceptions in the application.

@RestControllerAdvice
public class GlobalExceptionHandler  {
    
    // When a requested resource is not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // When trying to create a resource that already exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // When geometry data is invalid or cannot be processed
    @ExceptionHandler(InvalidGeometryException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidGeometry(InvalidGeometryException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // When validation on an argument annotated with @Valid fails (for example @NotNull, @Size, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // When the JSON in the request body is malformed or cannot be read
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid JSON format";
        
        // Provide more specific messages for common JSON issues
        if (ex.getCause() instanceof InvalidFormatException) {
            message = "Invalid data format in JSON request";
        }
        
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Handle common JTS geometry errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleJTSGeometryErrors(IllegalArgumentException ex) {
        String message = ex.getMessage();
        
        // Customize messages for common geometry issues
        if (message.contains("LinearRing") && message.contains("closed")) {
            message = "Polygon coordinates must form a closed shape (first and last point must be equal)";
        } else if (message.contains("points form a closed linestring")) {
            message = "Polygon must be closed: the first and last coordinates must be identical";
        }
        
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // When a database constraint is violated (e.g., unique constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Database constraint violation");
    }

    // Catch-all for any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    // Helper method to build a consistent response structure
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(body, status);
    }
}
