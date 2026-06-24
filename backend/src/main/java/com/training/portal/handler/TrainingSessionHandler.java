package com.training.portal.handler;

import com.training.portal.dto.request.TrainingSessionCreateRequest;
import com.training.portal.dto.request.TrainingSessionUpdateRequest;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.dto.response.TrainingSessionResponse;
import com.training.portal.mapper.TrainingSessionMapper;
import com.training.portal.model.TrainingSession;
import com.training.portal.service.TrainingSessionService;
import com.training.portal.validation.TrainingSessionValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrainingSessionHandler {

    private final TrainingSessionService trainingSessionService;
    private final TrainingSessionValidationService validationService;
    private final TrainingSessionMapper trainingSessionMapper;

    public TrainingSessionResponse createTrainingSession(TrainingSessionCreateRequest request) {
        validationService.validateTitleUnique(request.title());

        TrainingSession trainingSession = trainingSessionMapper.toEntity(request);
        if (trainingSession.getId() == null) {
            trainingSession.setId(java.util.UUID.randomUUID());
        }
        trainingSession.setNewEntity(true);
        trainingSession.setCreatedAt(java.time.LocalDateTime.now());
        trainingSession.setUpdatedAt(java.time.LocalDateTime.now());
        TrainingSession savedSession = trainingSessionService.save(trainingSession);

        return trainingSessionMapper.toResponse(savedSession);
    }

    public TrainingSessionResponse getTrainingSession(UUID id) {
        TrainingSession trainingSession = trainingSessionService.findById(id);
        return trainingSessionMapper.toResponse(trainingSession);
    }

    public PageResponse<TrainingSessionResponse> getAllTrainingSessions(int page, int size) {
        Page<TrainingSession> sessionPage = trainingSessionService.findAll(PageRequest.of(page, size));

        List<TrainingSessionResponse> content = sessionPage.getContent()
                .stream()
                .map(trainingSessionMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                sessionPage.getNumber(),
                sessionPage.getSize(),
                sessionPage.getTotalElements(),
                sessionPage.getTotalPages(),
                sessionPage.isLast()
        );
    }

    public TrainingSessionResponse updateTrainingSession(UUID id, TrainingSessionUpdateRequest request) {
        validationService.validateTitleUniqueForUpdate(request.title(), id);

        TrainingSession existingSession = trainingSessionService.findById(id);
        trainingSessionMapper.updateEntityFromRequest(request, existingSession);
        existingSession.setUpdatedAt(java.time.LocalDateTime.now());

        TrainingSession updatedSession = trainingSessionService.save(existingSession);

        return trainingSessionMapper.toResponse(updatedSession);
    }

    public void deleteTrainingSession(UUID id) {
        TrainingSession trainingSession = trainingSessionService.findById(id);
        trainingSessionService.delete(trainingSession);
    }
}
