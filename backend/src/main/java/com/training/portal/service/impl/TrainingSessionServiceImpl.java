package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.TrainingSession;
import com.training.portal.repository.TrainingSessionRepository;
import com.training.portal.service.TrainingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TrainingSessionServiceImpl implements TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;

    @Override
    @Transactional
    public TrainingSession save(TrainingSession trainingSession) {
        return Objects.requireNonNull(trainingSessionRepository.save(trainingSession));
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingSession findById(UUID id) {
        TrainingSession trainingSession = trainingSessionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training session not found with id: " + id));
        trainingSession.setNewEntity(false);
        return trainingSession;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findAll(Pageable pageable) {
        return trainingSessionRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public void delete(TrainingSession trainingSession) {
        trainingSession.setDeleted(true);
        trainingSessionRepository.save(trainingSession);
    }
}
