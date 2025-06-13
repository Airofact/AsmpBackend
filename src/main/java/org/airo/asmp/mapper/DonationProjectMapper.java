package org.airo.asmp.mapper;

import org.airo.asmp.dto.donation.DonationProjectCreateDto;
import org.airo.asmp.dto.donation.DonationProjectUpdateDto;
import org.airo.asmp.model.donation.DonationProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for {@link DonationProject}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DonationProjectMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "currentAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DonationProject createDtoToEntity(DonationProjectCreateDto dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "currentAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(DonationProjectUpdateDto dto, @MappingTarget DonationProject entity);
}
