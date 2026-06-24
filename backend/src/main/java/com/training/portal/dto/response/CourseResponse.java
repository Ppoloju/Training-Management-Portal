package com.training.portal.dto.response;

import com.training.portal.constants.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing course details")
public record CourseResponse(

    @Schema(description = "The unique identifier of the course", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The title of the course", example = "Advanced Java Programming")
    String title,

    @Schema(description = "The description of the course", example = "A comprehensive course on advanced Java topics.")
    String description,

    @Schema(description = "The start date of the course", example = "2024-01-15")
    LocalDate startDate,

    @Schema(description = "The end date of the course", example = "2024-05-15")
    LocalDate endDate,

    @Schema(description = "The status of the course", example = "PUBLISHED")
    CourseStatus status,

    @Schema(description = "Timestamp when the course was created")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the course was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the course")
    String createdBy,

    @Schema(description = "User who last updated the course")
    String updatedBy
) {}
