package com.training.portal.service;

import com.training.portal.model.Enrollment;

import java.util.UUID;

public interface EnrollmentService {

    Enrollment save(Enrollment enrollment);

    Enrollment findById(UUID id);

    void delete(Enrollment enrollment);

    org.springframework.data.domain.Page<Enrollment> findAll(org.springframework.data.domain.Pageable pageable);
}
