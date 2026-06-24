package com.training.portal.service;

import com.training.portal.model.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TrainerService {
    
    Trainer save(Trainer trainer);
    
    Trainer findById(UUID id);
    
    Page<Trainer> findAll(Pageable pageable);
    
    void delete(Trainer trainer);
}
