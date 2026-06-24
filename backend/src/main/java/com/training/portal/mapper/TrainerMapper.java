package com.training.portal.mapper;

import com.training.portal.dto.request.TrainerCreateRequest;
import com.training.portal.dto.request.TrainerUpdateRequest;
import com.training.portal.dto.response.TrainerResponse;
import com.training.portal.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainerMapper {

    @Mapping(target = "newEntity", ignore = true)
    @Mapping(target = "isActive", source = "isActive")
    Trainer toEntity(TrainerCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "newEntity", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "active", source = "isActive")
    void updateEntityFromRequest(TrainerUpdateRequest request, @MappingTarget Trainer trainer);

    @Mapping(target = "isActive", source = "active")
    TrainerResponse toResponse(Trainer trainer);
}
