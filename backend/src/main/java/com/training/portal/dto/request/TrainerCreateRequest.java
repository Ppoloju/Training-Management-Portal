package com.training.portal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create a new trainer")
public record TrainerCreateRequest(

    @NotBlank(message = "First name is mandatory")
    @Schema(description = "The first name of the trainer", example = "Jane")
    String firstName,

    @NotBlank(message = "Last name is mandatory")
    @Schema(description = "The last name of the trainer", example = "Smith")
    String lastName,

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(description = "The email address of the trainer", example = "jane.smith@example.com")
    String email,

    @Schema(description = "The specialization of the trainer", example = "Java Development")
    String specialization,

    @NotNull(message = "Active status is mandatory")
    @Schema(description = "Whether the trainer is active", example = "true")
    Boolean isActive
) {}
