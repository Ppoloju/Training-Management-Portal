package com.training.portal.mapper;

import com.training.portal.dto.request.EmployeeCreateRequest;
import com.training.portal.dto.request.EmployeeUpdateRequest;
import com.training.portal.dto.response.EmployeeResponse;
import com.training.portal.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "newEntity", ignore = true)
    @Mapping(target = "isActive", source = "isActive")
    Employee toEntity(EmployeeCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "newEntity", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "active", source = "isActive")
    void updateEntityFromRequest(EmployeeUpdateRequest request, @MappingTarget Employee employee);

    @Mapping(target = "isActive", source = "active")
    EmployeeResponse toResponse(Employee employee);
}
