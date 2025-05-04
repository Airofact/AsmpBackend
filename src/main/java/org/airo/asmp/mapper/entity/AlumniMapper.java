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
	Alumni partialUpdate(AlumniUpdateDto alumniUpdateDto, @MappingTarget Alumni alumni);

	@Mapping(source = "addedById", target = "addedBy.id")
	Alumni toEntity(AlumniCreateDto alumniCreateDto);

	@Mapping(source = "addedBy.id", target = "addedById")
	AlumniCreateDto toDto1(Alumni alumni);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(source = "addedById", target = "addedBy.id")
	Alumni partialUpdate(AlumniCreateDto alumniCreateDto, @MappingTarget Alumni alumni);
}