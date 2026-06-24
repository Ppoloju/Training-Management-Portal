package com.training.portal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response containing employee details")
public record EmployeeResponse(

    @Schema(description = "The unique identifier of the employee", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The first name of the employee", example = "John")
    String firstName,

    @Schema(description = "The last name of the employee", example = "Doe")
    String lastName,

    @Schema(description = "The email address of the employee", example = "john.doe@example.com")
    String email,

    @Schema(description = "The department of the employee", example = "Engineering")
    String department,

    @Schema(description = "Whether the employee is active", example = "true")
    boolean isActive,

    @Schema(description = "Timestamp when the employee was created")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the employee was last updated")
    LocalDateTime updatedAt,

    @Schema(description = "User who created the employee")
    String createdBy,

    @Schema(description = "User who last updated the employee")
    String updatedBy
) {}
