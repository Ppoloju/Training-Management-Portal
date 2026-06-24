package com.training.portal.dto.request;

import com.training.portal.constants.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Request to create a new course")
public record CourseCreateRequest(

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "The title of the course", example = "Advanced Java Programming")
    String title,

    @Schema(description = "The description of the course", example = "A comprehensive course on advanced Java topics.")
    String description,

    @Schema(description = "The start date of the course", example = "2024-01-15")
    LocalDate startDate,

    @Schema(description = "The end date of the course", example = "2024-05-15")
    LocalDate endDate,

    @NotNull(message = "Status is mandatory")
    @Schema(description = "The status of the course", example = "DRAFT")
    CourseStatus status
) {}
