package com.training.portal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create a new employee")
public record EmployeeCreateRequest(

    @NotBlank(message = "First name is mandatory")
    @Schema(description = "The first name of the employee", example = "John")
    String firstName,

    @NotBlank(message = "Last name is mandatory")
    @Schema(description = "The last name of the employee", example = "Doe")
    String lastName,

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(description = "The email address of the employee", example = "john.doe@example.com")
    String email,

    @Schema(description = "The department of the employee", example = "Engineering")
    String department,

    @NotNull(message = "Active status is mandatory")
    @Schema(description = "Whether the employee is active", example = "true")
    Boolean isActive
) {}
