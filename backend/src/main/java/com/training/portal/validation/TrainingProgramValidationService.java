package com.training.portal.validation;

import com.training.portal.repository.TrainingProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingProgramValidationService {

    private final TrainingProgramRepository trainingProgramRepository;

    public void validateTitleUnique(String title) {
        if (trainingProgramRepository.existsByTitleAndDeletedFalse(title)) {
            throw new IllegalArgumentException("Training program with title '" + title + "' already exists");
        }
    }

    public void validateTitleUniqueForUpdate(String title, UUID id) {
        if (trainingProgramRepository.existsByTitleAndIdNotAndDeletedFalse(title, id)) {
            throw new IllegalArgumentException("Training program with title '" + title + "' already exists");
        }
    }
}
