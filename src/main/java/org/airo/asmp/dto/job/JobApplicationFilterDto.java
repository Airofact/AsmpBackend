package org.airo.asmp.dto.job;

import org.airo.asmp.model.job.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;


public record JobApplicationFilterDto(
        UUID id,                       
        UUID jobPostId,                
        UUID alumniId,                
        ApplicationStatus status,      
        LocalDateTime applyTime 
) {
}