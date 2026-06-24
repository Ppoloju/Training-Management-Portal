package com.training.portal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Request to create a new enrollment")
public record EnrollmentCreateRequest(

    @NotNull(message = "Employee ID is mandatory")
    @Schema(description = "The ID of the employee enrolling", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID employeeId,

    @NotNull(message = "Training session ID is mandatory")
    @Schema(description = "The ID of the training session", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainingSessionId
) {}
