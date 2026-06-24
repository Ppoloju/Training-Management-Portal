package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.Trainer;
import com.training.portal.repository.TrainerRepository;
import com.training.portal.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    @Transactional
    public Trainer save(Trainer trainer) {
        return Objects.requireNonNull(trainerRepository.save(trainer));
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer findById(UUID id) {
        Trainer trainer = trainerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + id));
        trainer.setNewEntity(false);
        return trainer;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trainer> findAll(Pageable pageable) {
        return trainerRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public void delete(Trainer trainer) {
        trainer.setDeleted(true);
        trainerRepository.save(trainer);
    }
}
