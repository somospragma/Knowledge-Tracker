package com.pragma.vc.tracker.usermanagement.infrastructure.web;

import com.pragma.vc.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import com.pragma.vc.tracker.usermanagement.domain.exception.DuplicateUserEmailException;
import com.pragma.vc.tracker.usermanagement.domain.exception.UserNotFoundException;
import com.pragma.vc.tracker.usermanagement.infrastructure.adapter.keycloak.KeycloakUserServiceAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for User Management REST API.
 * Converts domain and application exceptions into appropriate HTTP responses.
 */
@RestControllerAdvice(basePackages = "com.pragma.vc.tracker.usermanagement.infrastructure.web")
public class UserManagementExceptionHandler {

    @ExceptionHandler(DuplicateUserEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserEmail(DuplicateUserEmailException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Duplicate Email",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "User Not Found",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RegisterUserUseCase.UserRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(RegisterUserUseCase.UserRegistrationException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "User Registration Failed",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(KeycloakUserServiceAdapter.KeycloakUserCreationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakUserCreationException(
            KeycloakUserServiceAdapter.KeycloakUserCreationException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Keycloak Integration Error",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(KeycloakUserServiceAdapter.KeycloakRoleAssignmentException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakRoleAssignmentException(
            KeycloakUserServiceAdapter.KeycloakRoleAssignmentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Keycloak Role Assignment Error",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Input",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Standard error response structure
     */
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private Instant timestamp;

        public ErrorResponse(int status, String error, String message, Instant timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
        }

        // Getters
        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public Instant getTimestamp() {
            return timestamp;
        }
    }

    /**
     * Validation error response with field-level errors
     */
    public static class ValidationErrorResponse {
        private int status;
        private String error;
        private Map<String, String> fieldErrors;
        private Instant timestamp;

        public ValidationErrorResponse(int status, String error, Map<String, String> fieldErrors, Instant timestamp) {
            this.status = status;
            this.error = error;
            this.fieldErrors = fieldErrors;
            this.timestamp = timestamp;
        }

        // Getters
        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }

        public Instant getTimestamp() {
            return timestamp;
        }
    }
}
