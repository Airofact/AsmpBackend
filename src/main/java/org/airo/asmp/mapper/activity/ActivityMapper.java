package org.airo.asmp.mapper.activity;

import org.airo.asmp.dto.activity.ActivityCreateDto;
import org.airo.asmp.dto.activity.ActivityUpdateDto;
import org.airo.asmp.model.activity.Activity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapper {
	@Mapping(source = "organizer", target = "organizer.id")
	Activity toEntity(ActivityCreateDto activityCreateDto);

	@Mapping(source = "organizer.id", target = "organizer")
	ActivityCreateDto toDto(Activity activity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(source = "organizer", target = "organizer.id")
	Activity partialUpdate(ActivityCreateDto activityCreateDto, @MappingTarget Activity activity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(ActivityUpdateDto activityUpdateDto, @MappingTarget Activity activity);
}