package com.reportsaathi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler — catches every exception thrown anywhere in the app
 * and converts it to a clean JSON error response.
 *
 * Without this, Spring returns an ugly HTML error page that React Native can't parse.
 *
 * @RestControllerAdvice applies this to ALL controllers automatically.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles our custom exception (thrown when OCR or AI processing fails).
     */
    @ExceptionHandler(ReportProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleReportProcessingException(
            ReportProcessingException ex) {
        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    /**
     * Handles @Valid annotation failures (e.g. missing required fields in request body).
     * Returns a map of fieldName -> errorMessage so the app can highlight the right field.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("fields", fieldErrors);
        body.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Catch-all: any other unhandled exception returns 500 with a safe message.
     * We never expose stack traces to the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        // Log the real error on server side (visible in Railway/Render logs)
        System.err.println("[ReportSaathi] Unhandled exception: " + ex.getMessage());
        ex.printStackTrace();

        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
            "Something went wrong. Please try again.");
    }

    // ── Helper ────────────────────────────────────────────────────────────
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(status).body(body);
    }
}
