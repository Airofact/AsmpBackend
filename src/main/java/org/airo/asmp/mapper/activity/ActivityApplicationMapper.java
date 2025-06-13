package org.airo.asmp.mapper.activity;

import org.airo.asmp.dto.activity.ActivityApplicationCreateDto;
import org.airo.asmp.dto.activity.ActivityApplicationUpdateDto;
import org.airo.asmp.model.activity.ActivityApplication;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityApplicationMapper {
    @Mapping(source = "activityId", target = "activity.id")
    @Mapping(source = "alumniId", target = "alumni.id")
    ActivityApplication toEntity(ActivityApplicationCreateDto dto);

    @Mapping(source = "activity.id", target = "activityId")
    @Mapping(source = "alumni.id", target = "alumniId")
    ActivityApplicationCreateDto toDto(ActivityApplication entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "activityId", target = "activity.id")
    @Mapping(source = "alumniId", target = "alumni.id")
    void partialUpdate(ActivityApplicationUpdateDto dto, @MappingTarget ActivityApplication entity);
}
