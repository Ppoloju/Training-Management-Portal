package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.TrainingProgram;
import com.training.portal.repository.TrainingProgramRepository;
import com.training.portal.service.TrainingProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    @Override
    @Transactional
    public TrainingProgram save(TrainingProgram trainingProgram) {
        return Objects.requireNonNull(trainingProgramRepository.save(trainingProgram));
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingProgram findById(UUID id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program not found with id: " + id));
        trainingProgram.setNewEntity(false);
        return trainingProgram;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingProgram> findAll(Pageable pageable) {
        return trainingProgramRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public void delete(TrainingProgram trainingProgram) {
        trainingProgram.setDeleted(true);
        trainingProgramRepository.save(trainingProgram);
    }
}
