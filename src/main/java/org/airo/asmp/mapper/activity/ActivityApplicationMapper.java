package org.airo.asmp.mapper.activity;

import org.airo.asmp.dto.activity.ActivityApplicationCreateDto;
import org.airo.asmp.dto.activity.ActivityApplicationUpdateDto;
import org.airo.asmp.model.activity.ActivityApplication;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityApplicationMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "alumni", ignore = true)
    @Mapping(target = "applyTime", ignore = true)
    ActivityApplication toEntity(ActivityApplicationCreateDto dto);

    @Mapping(source = "alumni.id", target = "alumniId")
    ActivityApplicationCreateDto toDto(ActivityApplication entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(ActivityApplicationUpdateDto dto, @MappingTarget ActivityApplication entity);
}
