package com.training.portal.mapper;

import com.training.portal.dto.request.TrainingSessionCreateRequest;
import com.training.portal.dto.request.TrainingSessionUpdateRequest;
import com.training.portal.dto.response.TrainingSessionResponse;
import com.training.portal.model.TrainingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingSessionMapper {

    @Mapping(target = "newEntity", ignore = true)
    TrainingSession toEntity(TrainingSessionCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(TrainingSessionUpdateRequest request, @MappingTarget TrainingSession trainingSession);

    TrainingSessionResponse toResponse(TrainingSession trainingSession);
}
