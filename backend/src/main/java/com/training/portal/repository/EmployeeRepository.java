package com.training.portal.repository;

import com.training.portal.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends CrudRepository<Employee, UUID>, PagingAndSortingRepository<Employee, UUID> {
    
    Optional<Employee> findByIdAndDeletedFalse(UUID id);
    
    Page<Employee> findAllByDeletedFalse(Pageable pageable);
    
    boolean existsByEmailAndDeletedFalse(String email);
    
    boolean existsByEmailAndIdNotAndDeletedFalse(String email, UUID id);
    
}
