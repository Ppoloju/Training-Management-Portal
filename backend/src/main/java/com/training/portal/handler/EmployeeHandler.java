package com.training.portal.handler;

import com.training.portal.dto.request.EmployeeCreateRequest;
import com.training.portal.dto.request.EmployeeUpdateRequest;
import com.training.portal.dto.response.EmployeeResponse;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.mapper.EmployeeMapper;
import com.training.portal.model.Employee;
import com.training.portal.service.EmployeeService;
import com.training.portal.validation.EmployeeValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmployeeHandler {

    private final EmployeeService employeeService;
    private final EmployeeValidationService validationService;
    private final EmployeeMapper employeeMapper;

    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        validationService.validateEmailUnique(request.email());

        Employee employee = employeeMapper.toEntity(request);
        if (employee.getId() == null) {
            employee.setId(java.util.UUID.randomUUID());
        }
        if (request.isActive() != null) {
            employee.setActive(request.isActive());
        }
        employee.setNewEntity(true);
        employee.setCreatedAt(java.time.LocalDateTime.now());
        employee.setUpdatedAt(java.time.LocalDateTime.now());
        Employee savedEmployee = employeeService.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    public EmployeeResponse getEmployee(UUID id) {
        Employee employee = employeeService.findById(id);
        return employeeMapper.toResponse(employee);
    }

    public PageResponse<EmployeeResponse> getAllEmployees(int page, int size) {
        Page<Employee> employeePage = employeeService.findAll(PageRequest.of(page, size));
        
        List<EmployeeResponse> content = employeePage.getContent()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );
    }

    public EmployeeResponse updateEmployee(UUID id, EmployeeUpdateRequest request) {
        validationService.validateEmailUniqueForUpdate(request.email(), id);

        Employee existingEmployee = employeeService.findById(id);
        employeeMapper.updateEntityFromRequest(request, existingEmployee);
        existingEmployee.setUpdatedAt(java.time.LocalDateTime.now());

        Employee updatedEmployee = employeeService.save(existingEmployee);

        return employeeMapper.toResponse(updatedEmployee);
    }

    public EmployeeResponse activateEmployee(UUID id) {
        Employee employee = employeeService.findById(id);
        employee.setActive(true);
        Employee updatedEmployee = employeeService.save(employee);
        return employeeMapper.toResponse(updatedEmployee);
    }

    public EmployeeResponse deactivateEmployee(UUID id) {
        Employee employee = employeeService.findById(id);
        employee.setActive(false);
        Employee updatedEmployee = employeeService.save(employee);
        return employeeMapper.toResponse(updatedEmployee);
    }

    public void deleteEmployee(UUID id) {
        Employee employee = employeeService.findById(id);
        employeeService.delete(employee);
    }
}
