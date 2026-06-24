package com.training.portal.repository;

import com.training.portal.model.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrainerRepository extends CrudRepository<Trainer, UUID>, PagingAndSortingRepository<Trainer, UUID> {
    
    Optional<Trainer> findByIdAndDeletedFalse(UUID id);
    
    Page<Trainer> findAllByDeletedFalse(Pageable pageable);
    
    boolean existsByEmailAndDeletedFalse(String email);
    
    boolean existsByEmailAndIdNotAndDeletedFalse(String email, UUID id);
}
