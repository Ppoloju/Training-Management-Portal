package com.training.portal.validation;

import com.training.portal.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentValidationService {

    private final EnrollmentRepository enrollmentRepository;

    public void validateEnrollmentNotExists(String employeeId, String trainingSessionId) {
        if (enrollmentRepository.existsByEmployeeIdAndTrainingSessionIdAndDeletedFalse(UUID.fromString(employeeId), UUID.fromString(trainingSessionId))) {
            throw new IllegalArgumentException("Enrollment for employee '" + employeeId + "' in training session '" + trainingSessionId + "' already exists");
        }
    }
}
