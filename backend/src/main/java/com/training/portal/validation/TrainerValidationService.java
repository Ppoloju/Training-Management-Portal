package com.training.portal.validation;

import com.training.portal.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerValidationService {

    private final TrainerRepository trainerRepository;

    public void validateEmailUnique(String email) {
        if (trainerRepository.existsByEmailAndDeletedFalse(email)) {
            throw new IllegalArgumentException("Trainer with email '" + email + "' already exists");
        }
    }

    public void validateEmailUniqueForUpdate(String email, UUID id) {
        if (trainerRepository.existsByEmailAndIdNotAndDeletedFalse(email, id)) {
            throw new IllegalArgumentException("Trainer with email '" + email + "' already exists");
        }
    }
}
