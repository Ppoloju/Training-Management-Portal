package com.training.portal.mapper;

import com.training.portal.dto.request.EnrollmentCreateRequest;
import com.training.portal.dto.response.EnrollmentResponse;
import com.training.portal.model.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EnrollmentMapper {

    @org.mapstruct.Mapping(target = "newEntity", ignore = true)
    Enrollment toEntity(EnrollmentCreateRequest request);

    @org.mapstruct.Mapping(target = "employeeName", ignore = true)
    @org.mapstruct.Mapping(target = "employeeDepartment", ignore = true)
    @org.mapstruct.Mapping(target = "trainingSessionTitle", ignore = true)
    @org.mapstruct.Mapping(target = "trainingProgramId", ignore = true)
    @org.mapstruct.Mapping(target = "trainingProgramTitle", ignore = true)
    EnrollmentResponse toResponse(Enrollment enrollment);

    void updateEntityFromRequest(com.training.portal.dto.request.EnrollmentUpdateRequest request, @org.mapstruct.MappingTarget Enrollment enrollment);
}
