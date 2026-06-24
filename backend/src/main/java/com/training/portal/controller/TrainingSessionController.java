package com.training.portal.controller;

import com.training.portal.dto.request.TrainingSessionCreateRequest;
import com.training.portal.dto.request.TrainingSessionUpdateRequest;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.dto.response.TrainingSessionResponse;
import com.training.portal.handler.TrainingSessionHandler;
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
@RequestMapping("/api/v1/training-sessions")
@RequiredArgsConstructor
@Tag(name = "Training Sessions", description = "Training Session management API")
public class TrainingSessionController {

    private final TrainingSessionHandler trainingSessionHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new training session")
    public TrainingSessionResponse createTrainingSession(@Valid @RequestBody TrainingSessionCreateRequest request) {
        return trainingSessionHandler.createTrainingSession(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a training session by ID")
    public TrainingSessionResponse getTrainingSession(
            @Parameter(description = "ID of the training session to retrieve") @PathVariable UUID id) {
        return trainingSessionHandler.getTrainingSession(id);
    }

    @GetMapping
    @Operation(summary = "Get all training sessions with pagination")
    public PageResponse<TrainingSessionResponse> getAllTrainingSessions(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return trainingSessionHandler.getAllTrainingSessions(page, size);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing training session")
    public TrainingSessionResponse updateTrainingSession(
            @Parameter(description = "ID of the training session to update") @PathVariable UUID id,
            @Valid @RequestBody TrainingSessionUpdateRequest request) {
        return trainingSessionHandler.updateTrainingSession(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete a training session")
    public void deleteTrainingSession(
            @Parameter(description = "ID of the training session to delete") @PathVariable UUID id) {
        trainingSessionHandler.deleteTrainingSession(id);
    }
}
