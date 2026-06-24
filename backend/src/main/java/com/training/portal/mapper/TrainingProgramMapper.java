package com.training.portal.mapper;

import com.training.portal.dto.request.TrainingProgramCreateRequest;
import com.training.portal.dto.request.TrainingProgramUpdateRequest;
import com.training.portal.dto.response.TrainingProgramResponse;
import com.training.portal.model.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingProgramMapper {

    @Mapping(target = "newEntity", ignore = true)
    TrainingProgram toEntity(TrainingProgramCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "newEntity", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(TrainingProgramUpdateRequest request, @MappingTarget TrainingProgram trainingProgram);

    TrainingProgramResponse toResponse(TrainingProgram trainingProgram);
}
