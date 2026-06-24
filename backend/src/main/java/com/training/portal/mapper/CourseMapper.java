package com.training.portal.mapper;

import com.training.portal.dto.request.CourseCreateRequest;
import com.training.portal.dto.request.CourseUpdateRequest;
import com.training.portal.dto.response.CourseResponse;
import com.training.portal.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    Course toEntity(CourseCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(CourseUpdateRequest request, @MappingTarget Course course);

    CourseResponse toResponse(Course course);
}
