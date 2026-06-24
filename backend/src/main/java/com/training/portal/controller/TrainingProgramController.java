package com.training.portal.controller;

import com.training.portal.dto.request.TrainingProgramCreateRequest;
import com.training.portal.dto.request.TrainingProgramUpdateRequest;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.dto.response.TrainingProgramResponse;
import com.training.portal.handler.TrainingProgramHandler;
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
@RequestMapping("/api/v1/training-programs")
@RequiredArgsConstructor
@Tag(name = "Training Programs", description = "Training Program management API")
public class TrainingProgramController {

    private final TrainingProgramHandler trainingProgramHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new training program")
    public TrainingProgramResponse createTrainingProgram(@Valid @RequestBody TrainingProgramCreateRequest request) {
        return trainingProgramHandler.createTrainingProgram(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a training program by ID")
    public TrainingProgramResponse getTrainingProgram(
            @Parameter(description = "ID of the training program to retrieve") @PathVariable UUID id) {
        return trainingProgramHandler.getTrainingProgram(id);
    }

    @GetMapping
    @Operation(summary = "Get all training programs with pagination")
    public PageResponse<TrainingProgramResponse> getAllTrainingPrograms(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return trainingProgramHandler.getAllTrainingPrograms(page, size);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing training program")
    public TrainingProgramResponse updateTrainingProgram(
            @Parameter(description = "ID of the training program to update") @PathVariable UUID id,
            @Valid @RequestBody TrainingProgramUpdateRequest request) {
        return trainingProgramHandler.updateTrainingProgram(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete a training program")
    public void deleteTrainingProgram(
            @Parameter(description = "ID of the training program to delete") @PathVariable UUID id) {
        trainingProgramHandler.deleteTrainingProgram(id);
    }
}
