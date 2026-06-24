package com.training.portal.repository;

import com.training.portal.model.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrainingSessionRepository extends CrudRepository<TrainingSession, UUID>, PagingAndSortingRepository<TrainingSession, UUID> {

    Optional<TrainingSession> findByIdAndDeletedFalse(UUID id);

    Page<TrainingSession> findAllByDeletedFalse(Pageable pageable);

    boolean existsByTitleAndDeletedFalse(String title);

    boolean existsByTitleAndIdNotAndDeletedFalse(String title, UUID id);
}
