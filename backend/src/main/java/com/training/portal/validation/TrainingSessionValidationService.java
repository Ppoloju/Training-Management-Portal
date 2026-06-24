package com.training.portal.validation;

import com.training.portal.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingSessionValidationService {

    private final TrainingSessionRepository trainingSessionRepository;

    public void validateTitleUnique(String title) {
        if (trainingSessionRepository.existsByTitleAndDeletedFalse(title)) {
            throw new IllegalArgumentException("Training session with title '" + title + "' already exists");
        }
    }

    public void validateTitleUniqueForUpdate(String title, UUID id) {
        if (trainingSessionRepository.existsByTitleAndIdNotAndDeletedFalse(title, id)) {
            throw new IllegalArgumentException("Training session with title '" + title + "' already exists");
        }
    }
}
