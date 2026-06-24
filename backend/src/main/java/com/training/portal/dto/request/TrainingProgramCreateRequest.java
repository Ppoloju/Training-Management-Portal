package com.training.portal.dto.request;

import com.training.portal.constants.TrainingProgramStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request to create a new training program")
public record TrainingProgramCreateRequest(

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "The title of the training program", example = "Spring Boot Bootcamp 2024")
    String title,

    @Schema(description = "The description of the training program", example = "An intensive bootcamp covering Spring Boot fundamentals and advanced topics.")
    String description,

    @Schema(description = "The start date of the training program", example = "2024-03-01")
    LocalDate startDate,

    @Schema(description = "The end date of the training program", example = "2024-06-01")
    LocalDate endDate,

    @NotNull(message = "Status is mandatory")
    @Schema(description = "The status of the training program", example = "PLANNED")
    TrainingProgramStatus status,

    @Schema(description = "Maximum number of participants", example = "30")
    Integer maxParticipants,

    @NotNull(message = "Trainer ID is mandatory")
    @Schema(description = "The ID of the assigned trainer", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainerId,

    @NotNull(message = "Course ID is mandatory")
    @Schema(description = "The ID of the associated course", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID courseId
) {}
