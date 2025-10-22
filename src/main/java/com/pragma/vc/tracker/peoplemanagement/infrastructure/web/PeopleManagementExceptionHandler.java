package com.pragma.vc.tracker.peoplemanagement.infrastructure.web;

import com.pragma.vc.tracker.peoplemanagement.domain.exception.ChapterNotFoundException;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.DuplicateChapterNameException;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.DuplicateEmailException;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.PragmaticNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception handler for People Management bounded context
 */
@RestControllerAdvice(basePackages = "com.pragma.vc.tracker.peoplemanagement.infrastructure.web")
public class PeopleManagementExceptionHandler {

    @ExceptionHandler(ChapterNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleChapterNotFound(ChapterNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(PragmaticNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePragmaticNotFound(PragmaticNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(DuplicateChapterNameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateChapterName(DuplicateChapterNameException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}