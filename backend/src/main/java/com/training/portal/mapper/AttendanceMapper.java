package com.training.portal.mapper;

import com.training.portal.dto.request.AttendanceCreateRequest;
import com.training.portal.dto.response.AttendanceResponse;
import com.training.portal.model.Attendance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AttendanceMapper {

    @org.mapstruct.Mapping(target = "newEntity", ignore = true)
    Attendance toEntity(AttendanceCreateRequest request);

    AttendanceResponse toResponse(Attendance attendance);

    void updateEntityFromRequest(com.training.portal.dto.request.AttendanceUpdateRequest request, @org.mapstruct.MappingTarget Attendance attendance);
}
