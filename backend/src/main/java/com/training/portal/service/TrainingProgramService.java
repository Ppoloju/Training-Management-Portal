package com.training.portal.service;

import com.training.portal.model.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TrainingProgramService {
    
    TrainingProgram save(TrainingProgram trainingProgram);
    
    TrainingProgram findById(UUID id);
    
    Page<TrainingProgram> findAll(Pageable pageable);
    
    void delete(TrainingProgram trainingProgram);
}
