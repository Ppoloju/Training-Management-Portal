package com.training.portal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing training session details")
public record TrainingSessionResponse(

    @Schema(description = "The unique identifier of the training session", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The title of the training session", example = "Spring Boot Review Session")
    String title,

    @Schema(description = "The description of the training session", example = "Review key Spring Boot concepts and hands-on labs.")
    String description,

    @Schema(description = "The start date of the training session", example = "2024-07-01")
    LocalDate startDate,

    @Schema(description = "The end date of the training session", example = "2024-07-05")
    LocalDate endDate,

    @Schema(description = "The ID of the associated training program", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainingProgramId,

    @Schema(description = "The location for the training session", example = "Room 201")
    String location,

    @Schema(description = "Maximum number of attendees", example = "50")
    Integer capacity,

    @Schema(description = "Timestamp when the training session was created")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the training session was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the training session")
    String createdBy,

    @Schema(description = "User who last updated the training session")
    String updatedBy
) {}
