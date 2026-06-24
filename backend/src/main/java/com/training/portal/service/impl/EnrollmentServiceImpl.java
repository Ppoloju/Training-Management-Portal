package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.Enrollment;
import com.training.portal.repository.EnrollmentRepository;
import com.training.portal.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public Enrollment save(Enrollment enrollment) {
        return Objects.requireNonNull(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public Enrollment findById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        enrollment.setNewEntity(false);
        return enrollment;
    }

    @Override
    @Transactional
    public void delete(Enrollment enrollment) {
        enrollment.setDeleted(true);
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Enrollment> findAll(org.springframework.data.domain.Pageable pageable) {
        return enrollmentRepository.findAllByDeletedFalse(pageable);
    }
}
