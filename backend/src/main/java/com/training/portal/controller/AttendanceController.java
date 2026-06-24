package com.training.portal.controller;

import com.training.portal.dto.request.AttendanceCreateRequest;
import com.training.portal.dto.response.AttendanceResponse;
import com.training.portal.handler.AttendanceHandler;
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
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
@Tag(name = "Attendances", description = "Attendance management API")
public class AttendanceController {

    private final AttendanceHandler attendanceHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new attendance record")
    public AttendanceResponse createAttendance(@Valid @RequestBody AttendanceCreateRequest request) {
        return attendanceHandler.createAttendance(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an attendance record by ID")
    public AttendanceResponse getAttendance(
            @Parameter(description = "ID of the attendance record to retrieve") @PathVariable UUID id) {
        return attendanceHandler.getAttendance(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete an attendance record")
    public void deleteAttendance(
            @Parameter(description = "ID of the attendance record to delete") @PathVariable UUID id) {
        attendanceHandler.deleteAttendance(id);
    }

    @GetMapping
    @Operation(summary = "Get all attendance records with pagination")
    public com.training.portal.dto.response.PageResponse<AttendanceResponse> getAllAttendances(
            @Parameter(description = "Page number (0-indexed)") @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size) {
        return attendanceHandler.getAllAttendances(page, size);
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}")
    @Operation(summary = "Update an existing attendance record")
    public AttendanceResponse updateAttendance(
            @Parameter(description = "ID of the attendance record to update") @PathVariable UUID id,
            @Valid @RequestBody com.training.portal.dto.request.AttendanceUpdateRequest request) {
        return attendanceHandler.updateAttendance(id, request);
    }
}
