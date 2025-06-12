package org.airo.asmp.dto.entity;

import jakarta.validation.constraints.NotNull;
import org.airo.asmp.model.job.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record JobApplicationCreateDto(
        UUID jobPostId,      
        UUID alumniId,        
        LocalDateTime applyTime,            
        ApplicationStatus status        
) {
}