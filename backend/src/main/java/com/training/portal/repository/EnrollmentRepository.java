package com.training.portal.repository;

import com.training.portal.model.Enrollment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EnrollmentRepository extends CrudRepository<Enrollment, UUID>, PagingAndSortingRepository<Enrollment, UUID> {

    Optional<Enrollment> findByIdAndDeletedFalse(UUID id);

    boolean existsByEmployeeIdAndTrainingSessionIdAndDeletedFalse(UUID employeeId, UUID trainingSessionId);
    
    Page<Enrollment> findAllByDeletedFalse(Pageable pageable);
}
