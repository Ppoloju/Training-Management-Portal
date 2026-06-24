package com.training.portal.service;

import com.training.portal.model.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TrainingSessionService {

    TrainingSession save(TrainingSession trainingSession);

    TrainingSession findById(UUID id);

    Page<TrainingSession> findAll(Pageable pageable);

    void delete(TrainingSession trainingSession);
}
