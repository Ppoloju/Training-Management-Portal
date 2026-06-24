package com.training.portal.controller;

import com.training.portal.dto.request.CourseCreateRequest;
import com.training.portal.dto.request.CourseUpdateRequest;
import com.training.portal.dto.response.CourseResponse;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.handler.CourseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management API")
public class CourseController {

    private final CourseHandler courseHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course")
    public CourseResponse createCourse(@Valid @RequestBody CourseCreateRequest request) {
        return courseHandler.createCourse(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a course by ID")
    public CourseResponse getCourse(
            @Parameter(description = "ID of the course to retrieve") @PathVariable UUID id) {
        return courseHandler.getCourse(id);
    }

    @GetMapping
    @Operation(summary = "Get all courses with pagination")
    public PageResponse<CourseResponse> getAllCourses(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return courseHandler.getAllCourses(page, size);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing course")
    public CourseResponse updateCourse(
            @Parameter(description = "ID of the course to update") @PathVariable UUID id,
            @Valid @RequestBody CourseUpdateRequest request) {
        return courseHandler.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete a course")
    public void deleteCourse(
            @Parameter(description = "ID of the course to delete") @PathVariable UUID id) {
        courseHandler.deleteCourse(id);
    }
}
