package com.training.portal.controller;

import com.training.portal.dto.request.EnrollmentCreateRequest;
import com.training.portal.dto.response.EnrollmentResponse;
import com.training.portal.handler.EnrollmentHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Enrollment management API")
public class EnrollmentController {

    private final EnrollmentHandler enrollmentHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new enrollment")
    public EnrollmentResponse createEnrollment(@Valid @RequestBody EnrollmentCreateRequest request) {
        return enrollmentHandler.createEnrollment(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an enrollment by ID")
    public EnrollmentResponse getEnrollment(
            @Parameter(description = "ID of the enrollment to retrieve") @PathVariable UUID id) {
        return enrollmentHandler.getEnrollment(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete an enrollment")
    public void deleteEnrollment(
            @Parameter(description = "ID of the enrollment to delete") @PathVariable UUID id) {
        enrollmentHandler.deleteEnrollment(id);
    }

    @GetMapping
    @Operation(summary = "Get all enrollments with pagination")
    public com.training.portal.dto.response.PageResponse<EnrollmentResponse> getAllEnrollments(
            @Parameter(description = "Page number (0-indexed)") @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size) {
        return enrollmentHandler.getAllEnrollments(page, size);
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}")
    @Operation(summary = "Update an existing enrollment")
    public EnrollmentResponse updateEnrollment(
            @Parameter(description = "ID of the enrollment to update") @PathVariable UUID id,
            @Valid @RequestBody com.training.portal.dto.request.EnrollmentUpdateRequest request) {
        return enrollmentHandler.updateEnrollment(id, request);
    }
}
