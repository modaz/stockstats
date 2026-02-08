package com.stockstats.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadStatsRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadStatsRequestException(BadStatsRequestException ex) {
        return ResponseEntity.badRequest().body(buildErrorMap(400, ex));
    }

    @ExceptionHandler(SymbolNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSymbolNotFoundException(SymbolNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorMap(404, ex));
    }

    @ExceptionHandler(StatsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStatsNotFoundException(StatsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorMap(404, ex));
    }

    @ExceptionHandler(KitbotParallelProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleKitbotParallelProcessingException(KitbotParallelProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorMap(500, ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorMap(500, ex));
    }

    private Map<String, Object> buildErrorMap(int statusCode, Exception ex) {
        return Map.of(
                "status", statusCode,
                "message", ex.getMessage() != null ? ex.getMessage() : "Unknown error"
        );
    }
}
