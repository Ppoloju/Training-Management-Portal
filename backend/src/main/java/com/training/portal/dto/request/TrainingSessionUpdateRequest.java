package com.training.portal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request to update an existing training session")
public record TrainingSessionUpdateRequest(

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "The title of the training session", example = "Spring Boot Review Session")
    String title,

    @Schema(description = "The description of the training session", example = "Review key Spring Boot concepts and hands-on labs.")
    String description,

    @Schema(description = "The start date of the training session", example = "2024-07-01")
    LocalDate startDate,

    @Schema(description = "The end date of the training session", example = "2024-07-05")
    LocalDate endDate,

    @NotNull(message = "Training program ID is mandatory")
    @Schema(description = "The ID of the associated training program", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainingProgramId,

    @Schema(description = "The location for the training session", example = "Room 201")
    String location,

    @Schema(description = "Maximum number of attendees", example = "50")
    Integer capacity
) {}
