package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.AlumniCreateDto;
import org.airo.asmp.dto.entity.AlumniUpdateDto;
import org.airo.asmp.model.entity.Alumni;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlumniMapper {
	Alumni toEntity(AlumniUpdateDto alumniUpdateDto);

	AlumniUpdateDto toDto(Alumni alumni);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(AlumniUpdateDto alumniUpdateDto, @MappingTarget Alumni alumni);

	Alumni toEntity(AlumniCreateDto alumniCreateDto);

	AlumniCreateDto toDto1(Alumni alumni);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Alumni partialUpdate(AlumniCreateDto alumniCreateDto, @MappingTarget Alumni alumni);
}