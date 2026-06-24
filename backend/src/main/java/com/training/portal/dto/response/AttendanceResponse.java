package com.training.portal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing attendance details")
public record AttendanceResponse(

    @Schema(description = "The unique identifier of the attendance record", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The ID of the enrollment", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID enrollmentId,

    @Schema(description = "Timestamp when attendance was recorded")
    LocalDateTime attendedAt,

    @Schema(description = "Whether the attendee was present")
    boolean present,

    @Schema(description = "Timestamp when the attendance record was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the attendance record")
    String createdBy,

    @Schema(description = "User who last updated the attendance record")
    String updatedBy
) {}
