package com.training.portal.controller;

import com.training.portal.dto.request.TrainerCreateRequest;
import com.training.portal.dto.request.TrainerUpdateRequest;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.dto.response.TrainerResponse;
import com.training.portal.handler.TrainerHandler;
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
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainers", description = "Trainer management API")
public class TrainerController {

    private final TrainerHandler trainerHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new trainer")
    public TrainerResponse createTrainer(@Valid @RequestBody TrainerCreateRequest request) {
        return trainerHandler.createTrainer(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a trainer by ID")
    public TrainerResponse getTrainer(
            @Parameter(description = "ID of the trainer to retrieve") @PathVariable UUID id) {
        return trainerHandler.getTrainer(id);
    }

    @GetMapping
    @Operation(summary = "Get all trainers with pagination")
    public PageResponse<TrainerResponse> getAllTrainers(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return trainerHandler.getAllTrainers(page, size);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing trainer")
    public TrainerResponse updateTrainer(
            @Parameter(description = "ID of the trainer to update") @PathVariable UUID id,
            @Valid @RequestBody TrainerUpdateRequest request) {
        return trainerHandler.updateTrainer(id, request);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a trainer")
    public TrainerResponse activateTrainer(
            @Parameter(description = "ID of the trainer to activate") @PathVariable UUID id) {
        return trainerHandler.activateTrainer(id);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a trainer")
    public TrainerResponse deactivateTrainer(
            @Parameter(description = "ID of the trainer to deactivate") @PathVariable UUID id) {
        return trainerHandler.deactivateTrainer(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete a trainer")
    public void deleteTrainer(
            @Parameter(description = "ID of the trainer to delete") @PathVariable UUID id) {
        trainerHandler.deleteTrainer(id);
    }
}
