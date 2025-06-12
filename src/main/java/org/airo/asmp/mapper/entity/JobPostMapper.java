package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.JobPostCreateDto;
import org.airo.asmp.dto.entity.JobPostUpdateDto;
import org.airo.asmp.model.job.JobPost;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobPostMapper {
    
    @Mapping(source = "enterpriseId", target = "enterprise.id")
    JobPost toEntity(JobPostCreateDto jobPostCreateDto);

    @Mapping(source = "enterprise.id", target = "enterpriseId")
    JobPostCreateDto toDto(JobPost jobPost);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "enterpriseId", target = "enterprise.id")
    JobPost partialUpdate(JobPostCreateDto jobPostCreateDto, @MappingTarget JobPost jobPost);

    JobPost toEntity(JobPostUpdateDto jobPostUpdateDto);

    JobPostUpdateDto toUpdateDto(JobPost jobPost);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    JobPost partialUpdate(JobPostUpdateDto jobPostUpdateDto, @MappingTarget JobPost jobPost);
}