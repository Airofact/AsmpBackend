package org.airo.asmp.mapper;

import org.airo.asmp.dto.AdminDto;
import org.airo.asmp.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdminMapper {
	Admin toEntity(AdminDto adminDto);
}