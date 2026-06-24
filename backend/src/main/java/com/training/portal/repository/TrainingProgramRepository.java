package com.training.portal.repository;

import com.training.portal.model.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrainingProgramRepository extends CrudRepository<TrainingProgram, UUID>, PagingAndSortingRepository<TrainingProgram, UUID> {
    
    Optional<TrainingProgram> findByIdAndDeletedFalse(UUID id);
    
    Page<TrainingProgram> findAllByDeletedFalse(Pageable pageable);
    
    boolean existsByTitleAndDeletedFalse(String title);
    
    boolean existsByTitleAndIdNotAndDeletedFalse(String title, UUID id);
}
