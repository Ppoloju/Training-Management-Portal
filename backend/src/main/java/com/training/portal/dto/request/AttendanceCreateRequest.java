package com.training.portal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Request to create a new attendance record")
public record AttendanceCreateRequest(

    @NotNull(message = "Enrollment ID is mandatory")
    @Schema(description = "The ID of the enrollment", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID enrollmentId,

    @NotNull(message = "AttendedAt is mandatory")
    @Schema(description = "The date and time when attendance was recorded", example = "2024-07-01T09:00:00")
    LocalDateTime attendedAt,

    @NotNull(message = "Present flag is mandatory")
    @Schema(description = "Whether the attendee was present", example = "true")
    Boolean present
) {}
