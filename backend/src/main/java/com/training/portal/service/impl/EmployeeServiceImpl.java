package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.Employee;
import com.training.portal.repository.EmployeeRepository;
import com.training.portal.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Employee save(Employee employee) {
        return Objects.requireNonNull(employeeRepository.save(employee));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findById(UUID id) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setNewEntity(false);
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public void delete(Employee employee) {
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }
}
