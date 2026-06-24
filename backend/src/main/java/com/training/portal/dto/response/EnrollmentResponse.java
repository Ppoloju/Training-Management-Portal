package com.training.portal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing enrollment details")
public record EnrollmentResponse(

    @Schema(description = "The unique identifier of the enrollment", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The ID of the enrolled employee", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID employeeId,

    @Schema(description = "The name of the enrolled employee", example = "John Doe")
    String employeeName,

    @Schema(description = "The department of the enrolled employee", example = "Engineering")
    String employeeDepartment,

    @Schema(description = "The ID of the training session", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainingSessionId,

    @Schema(description = "The title of the training session", example = "Java Session 1")
    String trainingSessionTitle,

    @Schema(description = "The ID of the training program", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID trainingProgramId,

    @Schema(description = "The title of the training program", example = "Java Bootcamp")
    String trainingProgramTitle,

    @Schema(description = "Timestamp when the enrollment was created")
    LocalDateTime enrolledAt,

    @Schema(description = "Timestamp when the enrollment was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the enrollment")
    String createdBy,

    @Schema(description = "User who last updated the enrollment")
    String updatedBy
) {}
