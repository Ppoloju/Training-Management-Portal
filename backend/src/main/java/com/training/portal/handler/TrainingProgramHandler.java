package com.training.portal.handler;

import com.training.portal.dto.request.TrainingProgramCreateRequest;
import com.training.portal.dto.request.TrainingProgramUpdateRequest;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.dto.response.TrainingProgramResponse;
import com.training.portal.mapper.TrainingProgramMapper;
import com.training.portal.model.TrainingProgram;
import com.training.portal.service.TrainingProgramService;
import com.training.portal.validation.TrainingProgramValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrainingProgramHandler {

    private final TrainingProgramService trainingProgramService;
    private final TrainingProgramValidationService validationService;
    private final TrainingProgramMapper trainingProgramMapper;

    public TrainingProgramResponse createTrainingProgram(TrainingProgramCreateRequest request) {
        validationService.validateTitleUnique(request.title());

        TrainingProgram trainingProgram = trainingProgramMapper.toEntity(request);
        if (trainingProgram.getId() == null) {
            trainingProgram.setId(java.util.UUID.randomUUID());
        }
        trainingProgram.setNewEntity(true);
        trainingProgram.setCreatedAt(java.time.LocalDateTime.now());
        trainingProgram.setUpdatedAt(java.time.LocalDateTime.now());
        TrainingProgram savedProgram = trainingProgramService.save(trainingProgram);

        return trainingProgramMapper.toResponse(savedProgram);
    }

    public TrainingProgramResponse getTrainingProgram(UUID id) {
        TrainingProgram trainingProgram = trainingProgramService.findById(id);
        return trainingProgramMapper.toResponse(trainingProgram);
    }

    public PageResponse<TrainingProgramResponse> getAllTrainingPrograms(int page, int size) {
        Page<TrainingProgram> programPage = trainingProgramService.findAll(PageRequest.of(page, size));
        
        List<TrainingProgramResponse> content = programPage.getContent()
                .stream()
                .map(trainingProgramMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                programPage.getNumber(),
                programPage.getSize(),
                programPage.getTotalElements(),
                programPage.getTotalPages(),
                programPage.isLast()
        );
    }

    public TrainingProgramResponse updateTrainingProgram(UUID id, TrainingProgramUpdateRequest request) {
        validationService.validateTitleUniqueForUpdate(request.title(), id);

        TrainingProgram existingProgram = trainingProgramService.findById(id);
        trainingProgramMapper.updateEntityFromRequest(request, existingProgram);
        existingProgram.setUpdatedAt(java.time.LocalDateTime.now());

        TrainingProgram updatedProgram = trainingProgramService.save(existingProgram);

        return trainingProgramMapper.toResponse(updatedProgram);
    }

    public void deleteTrainingProgram(UUID id) {
        TrainingProgram trainingProgram = trainingProgramService.findById(id);
        trainingProgramService.delete(trainingProgram);
    }
}
