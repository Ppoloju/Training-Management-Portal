package com.training.portal.handler;

import com.training.portal.dto.request.TrainerCreateRequest;
import com.training.portal.dto.request.TrainerUpdateRequest;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.dto.response.TrainerResponse;
import com.training.portal.mapper.TrainerMapper;
import com.training.portal.model.Trainer;
import com.training.portal.service.TrainerService;
import com.training.portal.validation.TrainerValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrainerHandler {

    private final TrainerService trainerService;
    private final TrainerValidationService validationService;
    private final TrainerMapper trainerMapper;

    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        validationService.validateEmailUnique(request.email());

        Trainer trainer = trainerMapper.toEntity(request);
        if (trainer.getId() == null) {
            trainer.setId(java.util.UUID.randomUUID());
        }
        trainer.setActive(request.isActive());
        trainer.setNewEntity(true);
        trainer.setCreatedAt(java.time.LocalDateTime.now());
        trainer.setUpdatedAt(java.time.LocalDateTime.now());
        Trainer savedTrainer = trainerService.save(trainer);

        return trainerMapper.toResponse(savedTrainer);
    }

    public TrainerResponse getTrainer(UUID id) {
        Trainer trainer = trainerService.findById(id);
        return trainerMapper.toResponse(trainer);
    }

    public PageResponse<TrainerResponse> getAllTrainers(int page, int size) {
        Page<Trainer> trainerPage = trainerService.findAll(PageRequest.of(page, size));
        
        List<TrainerResponse> content = trainerPage.getContent()
                .stream()
                .map(trainerMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                trainerPage.getNumber(),
                trainerPage.getSize(),
                trainerPage.getTotalElements(),
                trainerPage.getTotalPages(),
                trainerPage.isLast()
        );
    }

    public TrainerResponse updateTrainer(UUID id, TrainerUpdateRequest request) {
        validationService.validateEmailUniqueForUpdate(request.email(), id);

        Trainer existingTrainer = trainerService.findById(id);
        trainerMapper.updateEntityFromRequest(request, existingTrainer);
        existingTrainer.setUpdatedAt(java.time.LocalDateTime.now());

        Trainer updatedTrainer = trainerService.save(existingTrainer);

        return trainerMapper.toResponse(updatedTrainer);
    }

    public TrainerResponse activateTrainer(UUID id) {
        Trainer trainer = trainerService.findById(id);
        trainer.setActive(true);
        Trainer updatedTrainer = trainerService.save(trainer);
        return trainerMapper.toResponse(updatedTrainer);
    }

    public TrainerResponse deactivateTrainer(UUID id) {
        Trainer trainer = trainerService.findById(id);
        trainer.setActive(false);
        Trainer updatedTrainer = trainerService.save(trainer);
        return trainerMapper.toResponse(updatedTrainer);
    }

    public void deleteTrainer(UUID id) {
        Trainer trainer = trainerService.findById(id);
        trainerService.delete(trainer);
    }
}
