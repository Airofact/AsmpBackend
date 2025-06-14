package org.airo.asmp.dto.job;

import org.airo.asmp.model.job.JobApplication;

import java.time.LocalDateTime;
import java.util.UUID;


public record JobApplicationFilterDto(
        UUID id,                       
        UUID jobPostId,                
        UUID alumniId,                
        JobApplication.ApplicationStatus status,
        LocalDateTime applyTime 
) {
}