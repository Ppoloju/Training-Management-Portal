package com.training.portal.service;

import com.training.portal.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeeService {
    
    Employee save(Employee employee);
    
    Employee findById(UUID id);
    
    Page<Employee> findAll(Pageable pageable);
    
    void delete(Employee employee);
}
