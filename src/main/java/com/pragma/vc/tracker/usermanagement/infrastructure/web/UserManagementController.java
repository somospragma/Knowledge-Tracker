package com.pragma.vc.tracker.usermanagement.infrastructure.web;

import com.pragma.vc.tracker.usermanagement.application.dto.RegisterUserCommand;
import com.pragma.vc.tracker.usermanagement.application.dto.UserDTO;
import com.pragma.vc.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User Management operations.
 * Input adapter in hexagonal architecture.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "User registration and management API - Manage system users and their roles")
public class UserManagementController {

    private final RegisterUserUseCase registerUserUseCase;

    public UserManagementController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user in both the local database and Keycloak identity provider. " +
                    "The user will be assigned the specified system role and can authenticate using the provided credentials."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (e.g., invalid email format, missing required fields)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with this email already exists",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error (e.g., Keycloak connection failure)",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterUserCommand command) {
        UserDTO user = registerUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
