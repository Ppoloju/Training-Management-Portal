package com.training.portal.dto.response;

import com.training.portal.constants.TrainingProgramStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing training program details")
public record TrainingProgramResponse(

    @Schema(description = "The unique identifier of the training program", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The title of the training program", example = "Spring Boot Bootcamp 2024")
    String title,

    @Schema(description = "The description of the training program", example = "An intensive bootcamp covering Spring Boot fundamentals and advanced topics.")
    String description,

    @Schema(description = "The start date of the training program", example = "2024-03-01")
    LocalDate startDate,

    @Schema(description = "The end date of the training program", example = "2024-06-01")
    LocalDate endDate,

    @Schema(description = "The status of the training program", example = "PLANNED")
    TrainingProgramStatus status,

    @Schema(description = "Maximum number of participants", example = "30")
    Integer maxParticipants,

    @Schema(description = "The ID of the assigned trainer", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainerId,

    @Schema(description = "The ID of the associated course", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID courseId,

    @Schema(description = "Timestamp when the training program was created")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the training program was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the training program")
    String createdBy,

    @Schema(description = "User who last updated the training program")
    String updatedBy
) {}
