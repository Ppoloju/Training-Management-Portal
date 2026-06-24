package com.training.portal.controller;

import com.training.portal.dto.request.EmployeeCreateRequest;
import com.training.portal.dto.request.EmployeeUpdateRequest;
import com.training.portal.dto.response.EmployeeResponse;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.handler.EmployeeHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee management API")
public class EmployeeController {

    private final EmployeeHandler employeeHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new employee")
    public EmployeeResponse createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
        return employeeHandler.createEmployee(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an employee by ID")
    public EmployeeResponse getEmployee(
            @Parameter(description = "ID of the employee to retrieve") @PathVariable UUID id) {
        return employeeHandler.getEmployee(id);
    }

    @GetMapping
    @Operation(summary = "Get all employees with pagination")
    public PageResponse<EmployeeResponse> getAllEmployees(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return employeeHandler.getAllEmployees(page, size);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing employee")
    public EmployeeResponse updateEmployee(
            @Parameter(description = "ID of the employee to update") @PathVariable UUID id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        return employeeHandler.updateEmployee(id, request);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate an employee")
    public EmployeeResponse activateEmployee(
            @Parameter(description = "ID of the employee to activate") @PathVariable UUID id) {
        return employeeHandler.activateEmployee(id);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an employee")
    public EmployeeResponse deactivateEmployee(
            @Parameter(description = "ID of the employee to deactivate") @PathVariable UUID id) {
        return employeeHandler.deactivateEmployee(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete an employee")
    public void deleteEmployee(
            @Parameter(description = "ID of the employee to delete") @PathVariable UUID id) {
        employeeHandler.deleteEmployee(id);
    }
}
