package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.JobApplicationCreateDto;
import org.airo.asmp.dto.entity.JobApplicationUpdateDto;
import org.airo.asmp.model.job.JobApplication;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobApplicationMapper {
    
    @Mapping(source = "jobPostId", target = "jobPost.id")
    @Mapping(source = "alumniId", target = "alumni.id")
    JobApplication toEntity(JobApplicationCreateDto jobApplicationCreateDto);

    @Mapping(source = "jobPost.id", target = "jobPostId")
    @Mapping(source = "alumni.id", target = "alumniId")
    JobApplicationCreateDto toDto(JobApplication jobApplication);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "jobPostId", target = "jobPost.id")
    @Mapping(source = "alumniId", target = "alumni.id")
    JobApplication partialUpdate(JobApplicationCreateDto jobApplicationCreateDto, @MappingTarget JobApplication jobApplication);

    JobApplication toEntity(JobApplicationUpdateDto jobApplicationUpdateDto);

    JobApplicationUpdateDto toUpdateDto(JobApplication jobApplication);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    JobApplication partialUpdate(JobApplicationUpdateDto jobApplicationUpdateDto, @MappingTarget JobApplication jobApplication);
}