package com.training.portal.validation;

import com.training.portal.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeValidationService {

    private final EmployeeRepository employeeRepository;

    public void validateEmailUnique(String email) {
        if (employeeRepository.existsByEmailAndDeletedFalse(email)) {
            throw new IllegalArgumentException("Employee with email '" + email + "' already exists");
        }
    }

    public void validateEmailUniqueForUpdate(String email, UUID id) {
        if (employeeRepository.existsByEmailAndIdNotAndDeletedFalse(email, id)) {
            throw new IllegalArgumentException("Employee with email '" + email + "' already exists");
        }
    }
}
