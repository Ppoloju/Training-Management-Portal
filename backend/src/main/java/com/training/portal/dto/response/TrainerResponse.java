package com.training.portal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing trainer details")
public record TrainerResponse(

    @Schema(description = "The unique identifier of the trainer", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The first name of the trainer", example = "Jane")
    String firstName,

    @Schema(description = "The last name of the trainer", example = "Smith")
    String lastName,

    @Schema(description = "The email address of the trainer", example = "jane.smith@example.com")
    String email,

    @Schema(description = "The specialization of the trainer", example = "Java Development")
    String specialization,

    @Schema(description = "Whether the trainer is active", example = "true")
    boolean isActive,

    @Schema(description = "Timestamp when the trainer was created")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the trainer was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the trainer")
    String createdBy,

    @Schema(description = "User who last updated the trainer")
    String updatedBy
) {}
