package com.training.portal.handler;

import com.training.portal.dto.request.EnrollmentCreateRequest;
import com.training.portal.dto.response.EnrollmentResponse;
import com.training.portal.mapper.EnrollmentMapper;
import com.training.portal.model.Enrollment;
import com.training.portal.repository.EmployeeRepository;
import com.training.portal.repository.TrainingProgramRepository;
import com.training.portal.repository.TrainingSessionRepository;
import com.training.portal.service.EnrollmentService;
import com.training.portal.validation.EnrollmentValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentHandler {

    private final EnrollmentService enrollmentService;
    private final EnrollmentValidationService validationService;
    private final EnrollmentMapper enrollmentMapper;
    private final EmployeeRepository employeeRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    public EnrollmentResponse createEnrollment(EnrollmentCreateRequest request) {
        validationService.validateEnrollmentNotExists(request.employeeId().toString(), request.trainingSessionId().toString());

        Enrollment enrollment = enrollmentMapper.toEntity(request);
        if (enrollment.getId() == null) {
            enrollment.setId(java.util.UUID.randomUUID());
        }
        enrollment.setNewEntity(true);
        enrollment.setEnrolledAt(java.time.LocalDateTime.now());
        enrollment.setCreatedAt(java.time.LocalDateTime.now());
        enrollment.setUpdatedAt(java.time.LocalDateTime.now());
        Enrollment savedEnrollment = enrollmentService.save(enrollment);

        return enrichEnrollmentResponse(enrollmentMapper.toResponse(savedEnrollment));
    }

    public EnrollmentResponse getEnrollment(java.util.UUID id) {
        Enrollment enrollment = enrollmentService.findById(id);
        return enrichEnrollmentResponse(enrollmentMapper.toResponse(enrollment));
    }

    public void deleteEnrollment(java.util.UUID id) {
        Enrollment enrollment = enrollmentService.findById(id);
        enrollmentService.delete(enrollment);
    }

    public com.training.portal.dto.response.PageResponse<EnrollmentResponse> getAllEnrollments(int page, int size) {
        org.springframework.data.domain.Page<Enrollment> enrollmentPage = enrollmentService.findAll(org.springframework.data.domain.PageRequest.of(page, size));
        
        java.util.List<EnrollmentResponse> content = enrollmentPage.getContent()
                .stream()
                .map(enrollment -> enrichEnrollmentResponse(enrollmentMapper.toResponse(enrollment)))
                .toList();

        return new com.training.portal.dto.response.PageResponse<>(
                content,
                enrollmentPage.getNumber(),
                enrollmentPage.getSize(),
                enrollmentPage.getTotalElements(),
                enrollmentPage.getTotalPages(),
                enrollmentPage.isLast()
        );
    }

    public EnrollmentResponse updateEnrollment(java.util.UUID id, com.training.portal.dto.request.EnrollmentUpdateRequest request) {
        Enrollment existingEnrollment = enrollmentService.findById(id);
        enrollmentMapper.updateEntityFromRequest(request, existingEnrollment);
        existingEnrollment.setUpdatedAt(java.time.LocalDateTime.now());

        Enrollment updatedEnrollment = enrollmentService.save(existingEnrollment);

        return enrichEnrollmentResponse(enrollmentMapper.toResponse(updatedEnrollment));
    }

    private EnrollmentResponse enrichEnrollmentResponse(EnrollmentResponse response) {
        if (response == null) return null;

        String employeeName = null;
        String employeeDepartment = null;
        String trainingSessionTitle = null;
        java.util.UUID trainingProgramId = null;
        String trainingProgramTitle = null;

        var employee = employeeRepository.findById(response.employeeId()).orElse(null);
        if (employee != null) {
            employeeName = employee.getFirstName() + " " + employee.getLastName();
            employeeDepartment = employee.getDepartment();
        }

        var session = trainingSessionRepository.findById(response.trainingSessionId()).orElse(null);
        if (session != null) {
            trainingSessionTitle = session.getTitle();
            trainingProgramId = session.getTrainingProgramId();
        }

        if (trainingProgramId != null) {
            var program = trainingProgramRepository.findById(trainingProgramId).orElse(null);
            if (program != null) {
                trainingProgramTitle = program.getTitle();
            }
        }

        return new EnrollmentResponse(
            response.id(),
            response.employeeId(),
            employeeName,
            employeeDepartment,
            response.trainingSessionId(),
            trainingSessionTitle,
            trainingProgramId,
            trainingProgramTitle,
            response.enrolledAt(),
            response.updatedAt(),
            response.createdBy(),
            response.updatedBy()
        );
    }
}
